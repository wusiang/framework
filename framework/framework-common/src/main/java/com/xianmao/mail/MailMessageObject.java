package com.xianmao.mail;

import java.io.File;
import java.util.List;

/**
 * @ClassName MailMessageObject
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-31 12:02
 * @Version 1.0
 */
public class MailMessageObject {
    private String subject;
    private String context;
    private List<File> fileList;
    private List<MailAddress> to;
    private List<MailAddress> cc;

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return the to
     */
    public List<MailAddress> getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(List<MailAddress> to) {
        this.to = to;
    }

    /**
     * @return the cc
     */
    public List<MailAddress> getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(List<MailAddress> cc) {
        this.cc = cc;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
}
