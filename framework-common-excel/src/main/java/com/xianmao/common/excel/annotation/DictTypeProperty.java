package com.xianmao.common.excel.annotation;

import com.xianmao.common.excel.vo.DictEnum;

import java.lang.annotation.*;

/**
 * Excel dict type 属性
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DictTypeProperty {

	/**
	 * 字典类型字段
	 * @return {@link String }
	 */
	String value() default "";

	/**
	 * [有限]直接从系统字典枚举解析，不走缓存
	 * @return 与当前字典有关的系统字典枚举列表
	 */
	Class<? extends DictEnum>[] enums() default {};

}
