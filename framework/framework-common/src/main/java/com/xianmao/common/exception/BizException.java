package com.xianmao.common.exception;

import com.xianmao.common.enums.IEnum;
import com.xianmao.common.string.StringUtil;

/**
 * @ClassName BizException
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:36
 * @Version 1.0
 */
public class BizException extends AbstractException {

    public BizException(String message, int code) {
        super(message,code);
    }

    public BizException(String message) {
        super(message,500);
    }
}
