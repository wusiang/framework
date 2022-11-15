package com.xianmao.common.redo.clusterlock.dblock;


import com.xianmao.common.redo.bean.LockWrapper;
import com.xianmao.common.redo.bean.PessimisticLockDO;
import com.xianmao.common.redo.clusterlock.ClusterLockHandler;
import com.xianmao.common.redo.clusterlock.dblock.dao.PessimisticLockMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 数据库锁
 * 乐观锁用一张表，悲观锁用一张表
 *
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
public class DBClusterLockHandler implements ClusterLockHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockHandler.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 线程副本--锁队列
     */
    private ThreadLocal<LinkedList<LockWrapper>> lockWrapperThreadLocal = new ThreadLocal<LinkedList<LockWrapper>>();

    /**
     * 获取锁的线程名缓存，
     * key-> resource(锁资源)
     * value-> 线程名
     */
    private Map<String, String> acquireLockThreadName = new HashMap<>();

    @Autowired
    private PessimisticLockMapper pessimisticLockMapper;

    /**
     * 悲观锁-上锁
     * @param resourceName
     * @return
     */
    @Override
    public boolean lock(String resourceName) {

        if (hasAcquireLock(resourceName)){
            //当前线程已占有该锁，直接返回，无需再抢
            //记录重入次数
            addReentrantCount(resourceName);
            LOGGER.info("锁重入，锁资源：{}", resourceName);
            return true;
        }

        /**
         * 悲观锁上锁，不需要超时时间，因为锁完表之后，事务提交或者回滚之后就会释放，
         * 宕机了数据库连接断了之后，也会释放
         */
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection conn = sqlSession.getConnection();
        try {
            //关闭事务自动提交
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            LOGGER.info("关闭事务自动提交异常", throwables);
            return false;
        }

        String currentThreadName = Thread.currentThread().getName();
        //查询锁的参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("resource", resourceName);
        //死循环等待，获取不到锁则继续阻塞（注意这里的连接会阻塞在mysql服务端，mysql超时之后就会返回异常）
        while (true){
            try {
                //假如PessimisticLockMapper是通过xml文件定义，可以通过getMapper获取
//                PessimisticLockMapper mapper = sqlSession.getMapper(PessimisticLockMapper.class);
                PessimisticLockDO pessimisticLockDO = pessimisticLockMapper.acquireLock(paramMap);
                if (pessimisticLockDO == null){
                    //抢锁失败
                    LOGGER.info("抢锁失败，PessimisticLockDO为空，请检查数据库配置，是否存在该记录行。resource：{}, 当前线程{}",
                            resourceName, currentThreadName);
                    throw new RuntimeException(String.format("抢锁失败，PessimisticLockDO为空，请检查数据库配置，是否存在该记录行。resource：%s, 当前线程%s",
                            resourceName, currentThreadName));
                }else {
                    //抢锁成功，跳出循环
                    LockWrapper lockWrapper = LockWrapper.build(resourceName, sqlSession);
                    addToLockWrapperThreadLocal(lockWrapper);
                    acquireLockThreadName.put(resourceName, currentThreadName);
                    LOGGER.info("抢锁成功,锁资源：{},当前线程:{}", resourceName, currentThreadName);
                    break;
                }
            }catch (Exception e){
                //com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction
                if (e != null && e.getCause() != null /*&& e.getCause() instanceof MySQLTransactionRollbackException*/
                        && e.getCause().getMessage().contains("Lock wait timeout")){
                    LOGGER.info("出现抢锁超时，继续下一次尝试抢锁。当前抢锁线程{}, 当前占有锁线程为：{}",
                            Thread.currentThread().getName(), acquireLockThreadName.get(resourceName));
                }else {
                    LOGGER.error("出现非抢锁异常", e);
                    //将异常返回到上层业务层，暴露错误
                    throw new RuntimeException("出现非抢锁异常！");
                }
            }
            //这里不需要睡眠，因为阻塞是在mysql服务端，所以这里再次抢锁前不需要等待
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        return true;
    }

    private void addToLockWrapperThreadLocal(LockWrapper lockWrapper) {
        if (lockWrapperThreadLocal.get() == null){
            lockWrapperThreadLocal.set(new LinkedList<>());
        }
        LinkedList lockWrapperQueue = lockWrapperThreadLocal.get();
        //入栈
        lockWrapperQueue.push(lockWrapper);
    }

    /**
     * 添加重入次数
     * @param resourceName
     */
    private void addReentrantCount(String resourceName) {
        LinkedList<LockWrapper> lockWrapperQueue = lockWrapperThreadLocal.get();
        if (lockWrapperQueue != null){
            for (LockWrapper lockWrapper : lockWrapperQueue) {
                if (lockWrapper.getResourceName().contains(resourceName)){
                    //次数+1
                    lockWrapper.setReentrantCount(lockWrapper.getReentrantCount() + 1);
                    return;
                }
            }
        }
    }

    private boolean hasAcquireLock(String resourceName) {
        String acquireLockThread = acquireLockThreadName.get(resourceName);
        if (acquireLockThread != null && acquireLockThread.contains(Thread.currentThread().getName())){
            return true;
        }
        return false;
    }

    /**
     * 悲观锁-解锁
     * @return
     */
    @Override
    public boolean unlock(String resourceName) {

        //解锁时先判断是否存在锁重入
        LinkedList<LockWrapper> lockWrapperQueue = lockWrapperThreadLocal.get();
        if (lockWrapperQueue == null){
            return true;
        }
        if (lockWrapperQueue.size() > 0){
            //遍历锁队列
            for (LockWrapper lockWrapper : lockWrapperQueue) {
                if (lockWrapper.getResourceName().contains(resourceName)){
                    //判断次数是否大于0
                    if (lockWrapper.getReentrantCount() > 0){
                        //次数减一，返回
                        lockWrapper.setReentrantCount(lockWrapper.getReentrantCount() - 1);
                        return true;
                    }
                    //次数等于0，说明已经没有重入锁
                    break;
                }
            }

            //队列长度大于0，说明还有锁待释放
            //拿到栈顶元素
            LockWrapper lockWrapper = (LockWrapper) lockWrapperQueue.pollFirst();
            if (!lockWrapper.getResourceName().contains(resourceName)){
                //当前需要解锁的锁资源，必须是栈顶元素，假如不是说明程序调用有误，直接终止
                throw new RuntimeException("解锁异常，解锁顺序与上锁顺序不一致！");
            }
            SqlSession sqlSession = lockWrapper.getSqlSession();
            if (sqlSession != null){
                //提交事务，解锁！不提交也是可以的，回滚事务也可以。
                // 当conn为false时，调sqlSession.close()会将事务回滚
//                sqlSession.getConnection().commit();
                sqlSession.close();
                acquireLockThreadName.remove(resourceName);
            }
        }
        if (lockWrapperQueue.size() < 1){
            //无锁待释放了，清除threadlocal缓存
            lockWrapperThreadLocal.remove();
        }
        return true;
    }


}
