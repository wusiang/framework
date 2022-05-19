package com.xianmao.common.utils;

import com.xianmao.common.exception.BussinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @ClassName ValidatorUtil 参数校验器工具类
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:49
 * @Version 1.0
 */
public class ValidatorUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 如果命中校验错误则返回错误信息
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @return 错误信息
     */
    public static String returnAnyMessageIfError(Object object, Class... groups) {
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(object, groups);
        if (constraintViolationSet == null || constraintViolationSet.isEmpty()) {
            return "";
        }
        ConstraintViolation<Object> constraint = (ConstraintViolation<Object>) constraintViolationSet.iterator().next();
        return String.format("%s(%s)", constraint.getMessage(), constraint.getPropertyPath());
    }

    /**
     * 校验对象 如果命中校验错误则抛出异常
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws BussinessException 校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BussinessException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>) constraintViolations.iterator().next();
            throw new BussinessException(constraint.getMessage());
        }
    }
}
