package com.xianmao.common.redo.core;

import com.xianmao.common.core.utils.JsonUtils;
import com.xianmao.common.redo.bean.RedoReqParam;
import com.xianmao.common.redo.bean.RedoResult;
import com.xianmao.common.redo.bean.RedoTask;
import com.xianmao.common.redo.callback.RedoTaskCallback;
import com.xianmao.common.redo.clusterlock.LockControl;
import com.xianmao.common.redo.clusterlock.LockControlRegistry;
import com.xianmao.common.redo.common.exception.RedoTaskCallbackNotFoundException;
import com.xianmao.common.redo.dao.entity.RedoTaskDO;
import com.xianmao.common.redo.dao.mapper.RedoTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class RedoManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoManager.class);

    @Resource
    private RedoTaskMapper redoTaskMapper;

    @Autowired
    private LockControl systemDefaultLockControl;

    private String lockResourceName;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void init(){
        lockResourceName = buildLockResourceName();
    }

    private String buildLockResourceName() {
        return "com.xianmao.common.redo.core.RedoManager.findAndRedo:" + applicationName;
    }

    /**
     * 任务ID,可以根据内存里注册好的，找到相应的任务，然后入库
     * @param redoTaskId
     * @return
     */
    public boolean addRedoTask(String redoTaskId, String reqParam){
        //添加补偿任务入库
        RedoTask redoTask = RedoTaskRegisterFactory.findRedoTask(redoTaskId);
        RedoTaskDO redoTaskDO = new RedoTaskDO();
        BeanUtils.copyProperties(redoTask, redoTaskDO);
        redoTaskDO.setReqParam(reqParam);
        redoTaskDO.setApplicationName(applicationName);
        int res = redoTaskMapper.insert(redoTaskDO);
        return res > 0;
    }

    /**
     *
     * @param redoTaskId
     * @param redoReqParam
     * @return
     */
    public RedoResult invokeRedoTaskCallback(String redoTaskId, RedoReqParam redoReqParam){

        RedoResult redoResult;
        try {
            RedoTaskCallback redoTaskCallback = RedoTaskRegisterFactory.findRedoTaskCallback(redoTaskId);
            redoResult = redoTaskCallback.redo(redoReqParam);
        }catch (Exception e){
            LOGGER.error("invokeRedoTaskCallback error", e);
            //框架是一个组件，假如数据库加载的记录，在本应用中找不到对应的回调实例，说明不是本应用注册的，因此跳过执行
            if (e instanceof RedoTaskCallbackNotFoundException){
                //假如是这种异常，也可以是漏配了，也可能是别的应用注册，不是本应用该处理的，
                // 这种情况，是否应该让各个应用只关注自己相关的，在查询时假如应用标识
                return RedoResult.REDOTASK_CALLBACK_NOT_FOUND;
            }
            //假如执行业务时，再次抛出了异常，只给日志，向上层返回失败标志
            return RedoResult.BIZ_ERROR;
        }
        return redoResult;
    }

    public List<RedoTaskDO> selectRedoTaskList() {

        return redoTaskMapper.selectRedoTaskList(applicationName);
    }

    /**
     * //这个过程应该上锁，多节点互斥
     * <p>
     * 第一个方案，引入数据库锁进行互斥
     * 实现思路：每个微服务抢自己的锁，各个微服务之间不受影响，查库时，只查属于自己应用的记录
     * <p>
     * 第二个方案，是否能用select for update
     * 假如多个微服务都用select for update，锁之间可能会有干扰
     * 这种方案即使where条件里指定了app标识，只查本服务的待补偿记录，但是记录的顺序无法保证
     * 即使app标识加了索引，也无法保证锁一定会走行锁，所以多个服务之间还是会有互相影响
     * <p>
     * 第三个方案，上redis锁或者zk锁，也是可以的，
     * 假如锁出问题，会导致补偿任务被执行多次！业务中最好是加上防重的判断，例如状态机
     */
    public void findAndRedo() {

        //获取锁控制实例
        LockControl lockControl = getLockControlInstantce();
        //锁资源名称，每个微服务应该使用不同的锁名
        try {
            lockControl.lock(lockResourceName);
            //TODO 这里可以考虑给扩展点，让用户决定是否使用数据库作为保存补偿任务的介质，可以使用redis替代
            List<RedoTaskDO> redoTaskDOList = selectRedoTaskList();
            if (CollectionUtils.isEmpty(redoTaskDOList)){
                if (LOGGER.isTraceEnabled()){
                    LOGGER.trace("本次未发现待补偿任务");
                }
                return;
            }
            //这里可以引入线程池并行处理所有待补偿任务，提高处理速度 TODO

            redoTaskDOList.forEach(redoTaskDO -> {

                boolean isExpire = checkExpireDate(redoTaskDO.getExpiredDate());
                boolean isReachMaxAttempts = checkMaxAttempts(redoTaskDO);
                if (isExpire || isReachMaxAttempts){
                    deleteRedoTask(redoTaskDO);
                }else {
                    //该条记录未过期，开始执行补偿
                    RedoReqParam redoReqParam = JsonUtils.parse(redoTaskDO.getReqParam(), RedoReqParam.class);
                    RedoResult redoResult = invokeRedoTaskCallback(redoTaskDO.getRedoTaskId(), redoReqParam);
                    if (redoResult.isSuccess()){
                        //执行成功，删除数据库记录
                        deleteRedoTask(redoTaskDO);
                    }else {
                        //执行失败，这里应该立刻循环重试吗？还是先执行一次，然后等下一次从数据库查出之后再执行下一次
                        //假如这里立刻开启循环，会影响其他补偿任务，为了公平性，可以先重试一次
                        //这里可以设置一个饥饿策略，假如设置了饥饿策略，就循环重试 TODO

                        //执行次数 +1
                        updateExecTimes(redoTaskDO);
                    }
                }
            });
        }catch (Exception e){
            LOGGER.error("findAndRedo error", e);
        }finally {
            lockControl.unlock(lockResourceName);
        }
    }

    private LockControl getLockControlInstantce() {
        //用户扩展注册进来的锁控制实现，可以根据系统需要，替换不同的锁实现
        LockControl userLockControl = LockControlRegistry.getLockControl();
        return userLockControl != null? userLockControl : systemDefaultLockControl;
    }

    private boolean checkMaxAttempts(RedoTaskDO redoTaskDO) {
        return redoTaskDO.getExecTimes() >= redoTaskDO.getMaxAttempts();
    }

    private void updateExecTimes(RedoTaskDO redoTaskDO) {
        redoTaskMapper.updateExecTimes(redoTaskDO.getRedoTaskId());
    }

    private boolean checkExpireDate(Date expiredDate) {

        if (expiredDate == null){
            //假如未指定过期时间，默认为永不过期
            return false;
        }
        return new Date().after(expiredDate);
    }

    private void deleteRedoTask(RedoTaskDO redoTaskDO) {
        //TODO 将执行记录记到 补偿流水表，能反馈组件的执行情况，记录是被成功执行或是已超过待补偿期限
        recordRedoHistory(redoTaskDO);
        redoTaskMapper.delete(redoTaskDO.getRedoTaskId());
    }

    private void recordRedoHistory(RedoTaskDO redoTaskDO) {
        //TODO 这个可以设置开关，让用户自行选择，是否需要记补偿流水

    }


}
