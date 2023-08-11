package com.xianmao.common.excel.kit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

/**
 * 校验工具
 *
 * @author L.cm
 */
public final class Validators {

	private Validators() {
	}

	private static final Validator VALIDATOR;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		VALIDATOR = factory.getValidator();
	}

	public static <T> Set<ConstraintViolation<T>> validate(T object) {
		return VALIDATOR.validate(object);
	}

}
