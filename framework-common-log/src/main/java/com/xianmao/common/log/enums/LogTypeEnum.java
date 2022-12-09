package com.xianmao.common.log.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogTypeEnum {

	/**
	 * 正常日志类型
	 */
	INFO("INFO", "正常日志"),

	/**
	 * 错误日志类型
	 */
	ERROR("ERROR", "错误日志");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
