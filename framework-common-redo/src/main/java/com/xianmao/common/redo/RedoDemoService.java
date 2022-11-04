package com.xianmao.common.redo;

import com.xianmao.common.core.utils.JsonUtils;
import com.xianmao.common.redo.bean.RedoReqParam;
import com.xianmao.common.redo.bean.RedoResult;
import com.xianmao.common.redo.bean.RedoTask;
import com.xianmao.common.redo.callback.RedoTaskCallback;
import com.xianmao.common.redo.core.RedoManager;
import com.xianmao.common.redo.core.RedoTaskRegisterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo
 * 展示本组件的用法
 * author:xuyaokun_kzx
 * desc:
*/
//模拟一个普通的业务层
//@Service
public class RedoDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoDemoService.class);

    /**
     * 定义补偿任务的业务ID
     * 每一个补偿任务都应对应一个唯一的ID，建议有规则地命名，不要过长
     */
    private final String redoTaskIdOne = "redoBiz1";

    @Autowired
    private RedoManager redoManager;

    @PostConstruct
    public void init(){

        //定义补偿重试任务
        RedoTask redoTask = RedoTask.newBuilder(redoTaskIdOne).maxAttempts(3).build();
        //注册
        //这里可以使用匿名内部类，也可以单独建个新类，实现redo方法
        RedoTaskRegisterFactory.register(redoTask, new RedoTaskCallback() {
            @Override
            public RedoResult redo(RedoReqParam redoReqParam) {
                Map<String, Object> map = redoReqParam.getParams();
                String name = (String) map.get("name");
                String desc = (String) map.get("desc");
                doService(name, desc);
                //假如补偿失败，返回BIZ_ERROR，后续会继续重试
                return RedoResult.BIZ_ERROR;
                //假如补偿成功，已达到预期效果，则返回SUCCESS，告知组件业务已成功！可从库中移除该任务
//                return RedoResult.SUCCESS;
            }
        });

    }

    public void test1(){

        //do something

        boolean isNeedRedo = true;
        //假如出现了业务异常（需要开始补偿）
        if (isNeedRedo){
            //组织即将入库的参数，后续会使用该参数触发回调
            RedoReqParam redoReqParam = new RedoReqParam();
            Map<String, Object> map = new HashMap<>();
            map.put("name", "xyk");
            map.put("desc", "kunghsu");
            redoReqParam.setParams(map);
            //调用组件提供出来的工具类方法，添加回调任务
            redoManager.addRedoTask(redoTaskIdOne, JsonUtils.toJson(redoReqParam));
        }

    }

    /**
     * 具体的业务方法
     */
    public void doService(String name, String desc){

        LOGGER.debug("执行具体的业务逻辑");
        LOGGER.info("{},{}", name, desc);
    }


}
