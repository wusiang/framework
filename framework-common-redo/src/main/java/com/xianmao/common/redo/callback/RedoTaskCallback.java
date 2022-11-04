package com.xianmao.common.redo.callback;

import com.xianmao.common.redo.bean.RedoReqParam;
import com.xianmao.common.redo.bean.RedoResult;

/**
 * 重试逻辑接口
*/
@FunctionalInterface
public interface RedoTaskCallback {

    /**
     * 执行重试
     * @param redoReqParam
     * @return
     */
    RedoResult redo(RedoReqParam redoReqParam);

}
