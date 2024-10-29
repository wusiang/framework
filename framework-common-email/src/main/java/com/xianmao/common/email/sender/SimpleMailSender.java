package com.xianmao.common.email.sender;


import com.xianmao.common.email.BaseMailSender;
import com.xianmao.common.email.config.MailConfig;
import org.apache.commons.mail.EmailException;


public class SimpleMailSender extends BaseMailSender {

    public SimpleMailSender(MailConfig config) throws EmailException {
        super(config, true);
    }

    public SimpleMailSender(MailConfig config, boolean isHtml) throws EmailException {
        super(config, isHtml);
    }

    public SimpleMailSender(MailConfig config, boolean isHtml, String changeSender) throws EmailException {
        super(config, isHtml, changeSender);
    }

    public void setContent(String content) throws EmailException {
        if (isHtml) {
            htmlMail.setHtmlMsg(content);
        } else {
            htmlMail.setTextMsg(content);
        }
    }

    public void setSubject(String subject) throws EmailException {
        htmlMail.setSubject(subject);
    }
}
