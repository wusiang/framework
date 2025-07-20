package com.xianmao.common.web.controller;

import com.xianmao.common.entity.web.R;

/**
 * 控制层层通用数据处理
 */
public class BaseController {

    public <T> R<T> success(T data) {
        return R.success(data);
    }

    public R<Void> success() {
        return R.success();
    }

    public R<Void> fail(String msg) {
        return R.fail(msg);
    }

    public R<Void> fail() {
        return R.fail();
    }

}
