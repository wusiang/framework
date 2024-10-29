package com.xianmao.common.email.template;


public class Template {

	private String name;
	private String subject;
	private String body;

	public Template() {
	}

	public Template(String name, String subject, String body) {
		this.name = name;
		this.subject = subject;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "DefaultTemplateImpl [name=" + name + ", subject=" + subject + ", body=" + body + "]";
	}

}
