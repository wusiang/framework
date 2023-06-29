package com.xianmao.common.trace.utils;

import java.util.UUID;

/**
 * traceId 处理
 */
public class TraceIdUtils {

	/**
	 * 生产 traceId
	 */
	public static String traceIdString() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().replace("-", "");
		return getUUID(uuidStr, 16);
	}

	/**
	 * 处理 traceId 长度
	 */
	public static String getUUID(String uuid, int len) {
		if (0 >= len) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(uuid.charAt(i));
		}
		return sb.toString();
	}

}
