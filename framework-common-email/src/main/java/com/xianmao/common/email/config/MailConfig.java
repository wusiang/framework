package com.xianmao.common.email.config;

import lombok.Data;

@Data
public class MailConfig {
	/**
	 *  SMTP服务器
	 */
	private String host;
	/**
	 * SMTP服务器端口（默认：25）
	 */
	private int port;
	/**
	 * SMTP服务器SSL端口（默认：465）
	 */
	private int sSLPort;
	/**
	 * SMTP是否需要验证
	 */
	private boolean auth;
	/**
	 * 否开启调试模式
	 */
	private boolean debug;
	/**
	 * SMTP服务器连接超时时限（单位：毫秒）
	 */
	private int connectionTimeout;
	/**
	 * SMTP服务器响应超时时限（单位：毫秒）
	 */
	private int timeout;
	/**
	 * 编码
	 */
	private String charset;
	/**
	 * 发送邮箱
	 */
	private String sender;
	/**
	 * 发送账号
	 */
	private String username;
	/**
	 * 发送账号密码
	 */
	private String password;
	/**
	 * 默认接收邮箱
	 */
	private String receiver;
}
