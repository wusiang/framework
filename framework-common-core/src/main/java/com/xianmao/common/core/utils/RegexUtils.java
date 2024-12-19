package com.xianmao.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import com.xianmao.common.core.support.StringPool;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 正则校验通用工具类
 */
public class RegexUtils {

	/**
	 * 自定义字段校验
	 *
	 * @param value   字段值
	 * @param regex   正则表达式
	 * @param message 验证消息
	 * @return String
	 */
	public static String validateField(String value, String regex, String message) {
		if (!RegexUtil.match(regex, value)) {
			return message;
		}
		return StringPool.EMPTY;
	}

	/**
	 * 如果字段值为空，则设置一个默认值
	 *
	 * @param getter        字段的getter方法
	 * @param setter        字段的setter方法
	 * @param valueSupplier 默认值提供方法
	 */
	public static <T> void setValueIfBlank(Supplier<T> getter, Consumer<T> setter, Supplier<T> valueSupplier) {
		if (ObjectUtil.isEmpty(getter.get())) {
			setter.accept(valueSupplier.get());
		}
	}
}
