package com.xianmao.common.core.utils;


import com.xianmao.common.entity.exception.ServerErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidatorUtils {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    /**
     * 对指定对象进行参数校验，并返回校验结果列表
     *
     * @param object 待校验的对象
     * @return 校验结果列表，如果列表为空，表示校验通过
     */
    public static List<String> validate(Object object) {
        List<String> validationErrors = new ArrayList<>();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        for (ConstraintViolation<Object> violation : violations) {
            validationErrors.add(violation.getPropertyPath() + " " + violation.getMessage());
        }
        return validationErrors;
    }

    /**
     * 对指定对象进行参数校验，并抛出异常（如果校验不通过）
     *
     * @param object 待校验的对象
     * @throws IllegalArgumentException 如果校验不通过，抛出此异常，并包含所有校验错误信息
     */
    public static void validateAndThrow(Object object) {
        List<String> validationErrors = validate(object);
        if (!validationErrors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (String error : validationErrors) {
                errorMessage.append(error).append("\n");
            }
            throw ServerErrorCode.WARN.exp(errorMessage.toString());
        }
    }
}
