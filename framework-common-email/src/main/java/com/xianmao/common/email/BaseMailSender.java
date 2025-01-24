package com.xianmao.common.email;


import com.xianmao.common.email.config.MailConfig;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseMailSender implements Mail {

    protected HtmlEmail htmlMail;
    protected boolean isHtml = true;

    private BaseMailSender() {
    }

    public HtmlEmail getHtmlMail() {
        return htmlMail;
    }

    public BaseMailSender(MailConfig config, boolean isHtml) throws EmailException {
        htmlMail = new HtmlEmail();
        htmlMail.setHostName(config.getHost());
        htmlMail.setSmtpPort(config.getPort());
        htmlMail.setSSLOnConnect(config.isAuth());
        htmlMail.setSslSmtpPort(String.valueOf(config.getSSLPort()));
        htmlMail.setDebug(config.isDebug());
        htmlMail.setAuthentication(config.getUsername(), config.getPassword());
        htmlMail.setCharset(config.getCharset());
        htmlMail.setSocketConnectionTimeout(Duration.ofMillis(config.getConnectionTimeout()));
        htmlMail.setSocketTimeout(Duration.ofMillis(config.getTimeout()));
        // 设置默认发送人
        String sender = config.getSender();
        if (sender != null) {
            sender = sender.replaceAll("：", ":");
            if (sender.contains(":")) {
                String[] array = sender.split(":");
                setFrom(array[1].trim(), array[0].trim());
            } else {
                setFrom(sender);
            }
        }

        this.isHtml = isHtml;
    }

    public BaseMailSender(MailConfig config, boolean isHtml, String changeSender) throws EmailException {
        htmlMail = new HtmlEmail();
        htmlMail.setHostName(config.getHost());
        htmlMail.setSmtpPort(config.getPort());
        htmlMail.setSSLOnConnect(config.isAuth());
        htmlMail.setSslSmtpPort(String.valueOf(config.getSSLPort()));
        htmlMail.setDebug(config.isDebug());
        htmlMail.setAuthentication(config.getUsername(), config.getPassword());
        htmlMail.setCharset(config.getCharset());
        htmlMail.setSocketConnectionTimeout(Duration.ofMillis(config.getConnectionTimeout()));
        htmlMail.setSocketTimeout(Duration.ofMillis(config.getTimeout()));
        // 设置发送人
        String sender = changeSender;
        if (sender != null) {
            sender = sender.replaceAll("：", ":");
            if (sender.contains(":")) {
                String[] array = sender.split(":");
                setFrom(array[1].trim(), array[0].trim());
            } else {
                setFrom(sender);
            }
        }

        this.isHtml = isHtml;
    }

    @Override
    public void setSubject(String subject) throws EmailException {
        htmlMail.setSubject(subject);
    }

    @Override
    public void setContent(String content) throws EmailException {
        if (isHtml) {
            htmlMail.setHtmlMsg(content);
        } else {
            htmlMail.setTextMsg(content);
        }
    }

    @Override
    public void setFrom(String sender) throws EmailException {
        if (sender != null) {
            sender = sender.replaceAll("：", ":");
            if (sender.contains(":")) {
                String[] array = sender.split(":");
                htmlMail.setFrom(array[1].trim(), array[0].trim());
            } else {
                htmlMail.setFrom(sender);
            }
        }
    }

    @Override
    public void setFrom(String email, String name) throws EmailException {
        htmlMail.setFrom(email, name);
    }

    @Override
    public void setTo(String... toArray) throws EmailException, AddressException, UnsupportedEncodingException {
        List<InternetAddress> toList = new ArrayList<InternetAddress>();
        if (toArray != null) {
            for (String to : toArray) {
                to = to.replaceAll("：", ":");
                if (to.contains(":")) {
                    String[] array = to.split(":");
                    toList.add(new InternetAddress(array[1].trim(), array[0].trim()));
                } else {
                    toList.add(new InternetAddress(to));
                }
            }
            htmlMail.setTo(toList);
        }
    }

    public void setTo(List<String> toArray) throws EmailException, AddressException {
        List<InternetAddress> toList = new ArrayList<InternetAddress>();
        for (String to : toArray) {
            toList.add(new InternetAddress(to));
        }
        htmlMail.setTo(toList);
    }

    @Override
    public void setCc(String... ccArray) throws EmailException, AddressException, UnsupportedEncodingException {
        if (ccArray != null) {
            List<InternetAddress> ccList = new ArrayList<InternetAddress>();
            for (String cc : ccArray) {
                cc = cc.replaceAll("：", ":");
                if (cc.contains(":")) {
                    String[] array = cc.split(":");
                    ccList.add(new InternetAddress(array[1].trim(), array[0].trim()));
                } else {
                    ccList.add(new InternetAddress(cc));
                }
            }
            htmlMail.setCc(ccList);
        }
    }

    @Override
    public void setBcc(String... bccArray) throws EmailException, AddressException, UnsupportedEncodingException {
        if (bccArray != null) {
            List<InternetAddress> bccList = new ArrayList<InternetAddress>();
            for (String bcc : bccArray) {
                bcc = bcc.replaceAll("：", ":");
                if (bcc.contains(":")) {
                    String[] array = bcc.split(":");
                    bccList.add(new InternetAddress(array[1].trim(), array[0].trim()));
                } else {
                    bccList.add(new InternetAddress(bcc));
                }
            }
            htmlMail.setBcc(bccList);
        }
    }

    public void addAttache(URL url, String name, String desc) throws EmailException {
        htmlMail.attach(url, name, desc);
    }

    public void addAttache(File file) throws EmailException {
        htmlMail.attach(file);
    }

    public void addAttache(String path, String name, String desc) throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(path);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setName(name);
        attachment.setDescription(desc);
        htmlMail.attach(attachment);
    }
}
