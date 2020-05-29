package com.xianmao.captcha;

import com.xianmao.captcha.util.CaptchaUtils;
import com.xianmao.utils.ServletUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class CaptchaVO {
    /** 验证码 */
    String captcha;
    /** 验证码图片 */
    BufferedImage captchaImage;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public BufferedImage getCaptchaImage() {
        return captchaImage;
    }

    public void setCaptchaImage(BufferedImage captchaImage) {
        this.captchaImage = captchaImage;
    }

    /**
     * {@linkplain #captcha} set session
     * <p>{@linkplain #captchaImage} write response
     * <p>将验证码设置到session
     * <p>将验证码图片写入response，并设置ContentType为image/png
     */
    public void writeResponseAndSetSession() {
        HttpSession httpSession = ServletUtils.getSession();
        HttpServletResponse response = ServletUtils.getResponse();

        httpSession.setAttribute(CaptchaUtils.CAPTCHA_KEY, captcha);
        response.setContentType("image/png");

        OutputStream output;
        try {
            output = response.getOutputStream();
            ImageIO.write(captchaImage, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
