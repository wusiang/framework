package com.xianmao.common.excel.kit;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

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
