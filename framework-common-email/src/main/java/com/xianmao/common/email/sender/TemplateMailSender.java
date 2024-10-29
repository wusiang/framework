package com.xianmao.common.email.sender;


import com.xianmao.common.email.BaseMailSender;
import com.xianmao.common.email.config.MailConfig;
import com.xianmao.common.email.template.Template;
import com.xianmao.common.email.utils.FreeMarkerUtil;
import org.apache.commons.mail.EmailException;

import java.util.Map;


public class TemplateMailSender extends BaseMailSender {
	
	private final Template template;
	private final Map<String, Object> params;
	
	public TemplateMailSender(MailConfig config, Template template, Map<String, Object> params) throws EmailException {
		this(config, template, params, true);
	}
	
	public TemplateMailSender(MailConfig config, Template template, Map<String, Object> params, boolean isHtml) throws EmailException {
		super(config, isHtml);
		this.template = template;
		this.params = params;
		this.loadTemploadContent();
	}
	
	private void loadTemploadContent() throws EmailException {
		if(template == null) {
			return;
		}
		String subject = FreeMarkerUtil.template2String(template.getSubject(), params, false);
		setSubject(subject);
		String content = FreeMarkerUtil.template2String(template.getBody(), params, false);
		setContent(content);
	}
}
