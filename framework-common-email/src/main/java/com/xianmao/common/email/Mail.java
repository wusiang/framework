package com.xianmao.common.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;


public interface Mail {

	HtmlEmail getHtmlMail();
	
	void setSubject(String subject) throws EmailException;
	
	void setContent(String content) throws EmailException;

	void setFrom(String email) throws EmailException;
	
	void setFrom(String email, String name) throws EmailException;

	void setTo(String... to) throws EmailException, AddressException, UnsupportedEncodingException;

	void setCc(String... cc) throws EmailException, AddressException, UnsupportedEncodingException;

	void setBcc(String... bcc) throws EmailException, AddressException, UnsupportedEncodingException;

}
