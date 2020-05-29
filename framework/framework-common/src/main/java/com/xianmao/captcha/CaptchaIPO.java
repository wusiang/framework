package com.xianmao.captcha;

public class CaptchaIPO {
    /** 字符数量（默认值：4个字符） */
    private int charQuantity = 4;
    /** 图片宽度（默认值：100） */
    private int width = 100;
    /** 图片高度（默认值：36） */
    private int height = 36;
    /** 干扰线数量（默认值：5） */
    private int interferingLineQuantity = 5;
    /** 字体大小（默认值：30） */
    private int fontSize = 30;

    public int getCharQuantity() {
        return charQuantity;
    }

    public void setCharQuantity(int charQuantity) {
        this.charQuantity = charQuantity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getInterferingLineQuantity() {
        return interferingLineQuantity;
    }

    public void setInterferingLineQuantity(int interferingLineQuantity) {
        this.interferingLineQuantity = interferingLineQuantity;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
