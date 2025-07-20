# 邮箱组件使用说明



引入Pom
```
        <dependency>
            <groupId>com.xianmao</groupId>
            <artifactId>framework-common-email</artifactId>
        </dependency>
```

```text
# 账户配置信息
{"from": "添猫出行<tmcx@tianmaochuxing.com>", "host": "smtp.exmail.qq.com", "pass": "Tm13817077998", "user": "tmcx@tianmaochuxing.com"}
```

```java
package com.catcx.ride.hailing.message.server.sender.email.stmp;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.catcx.common.core.utils.JsonUtils;
import com.catcx.common.email.config.MailConfig;
import com.catcx.common.email.sender.SimpleMailSender;
import com.catcx.common.entity.exception.ServerErrorCode;
import com.catcx.ride.hailing.message.api.model.dto.EmailRequest;
import com.catcx.ride.hailing.message.api.model.vo.EmailResponse;
import com.catcx.ride.hailing.message.server.common.dto.SmtpAccountDTO;
import com.catcx.ride.hailing.message.server.common.enums.SenderEnum;
import com.catcx.ride.hailing.message.server.common.model.MessageSender;
import com.catcx.ride.hailing.message.server.common.utils.FileUtils;
import com.catcx.ride.hailing.message.server.sender.email.EamilSender;
import com.catcx.ride.hailing.message.server.sender.email.annotation.EamilSenderAnnotation;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

@Slf4j
@EamilSenderAnnotation(sender = SenderEnum.EMAIL_STMP)
public class StmpSender implements EamilSender {

    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest, MessageSender messageSender) {
        if (StrUtil.isBlank(messageSender.getAccountConfig())) {
            throw ServerErrorCode.WARN.exp("邮箱通道【"+messageSender.getSender()+"】账号未配置，无法发送邮件");
        }
        SmtpAccountDTO stmpConfig = JSON.parseObject(messageSender.getAccountConfig(), SmtpAccountDTO.class);
        MailConfig mailConfig = new MailConfig();
        mailConfig.setHost(stmpConfig.getHost());
        mailConfig.setAuth(true);
        mailConfig.setPort(25);
        mailConfig.setSSLPort(465);
        mailConfig.setDebug(true);
        mailConfig.setTimeout(10000);
        mailConfig.setConnectionTimeout(30000);
        mailConfig.setSender(stmpConfig.getFrom());
        mailConfig.setUsername(stmpConfig.getUser());
        mailConfig.setPassword(stmpConfig.getPass());
        mailConfig.setCharset("UTF-8");
        try {
            String sender = mailConfig.getSender();
            SimpleMailSender simpleMail = new SimpleMailSender(mailConfig, true, sender);
            simpleMail.setContent(emailRequest.getContent());
            simpleMail.setTo(emailRequest.getToUser());
            simpleMail.setSubject(emailRequest.getTitle());
            if (CollectionUtil.isNotEmpty(emailRequest.getAttachMap())) {
                for (Map.Entry<String, String> entry : emailRequest.getAttachMap().entrySet()) {
                    File file = FileUtils.getRemoteUrl2File("/Users/3y/temp", entry.getValue());
                    simpleMail.addAttache(file.getPath(), entry.getKey(), entry.getKey());
                }
            }
            simpleMail.getHtmlMail().send();
            log.info("邮箱发送成功:{}", JsonUtils.toJson(emailRequest));
        } catch (Exception e) {
            log.error("邮箱发送失败:{}", JsonUtils.toJson(emailRequest), e);
            return EmailResponse.builder().success(false).message(e.getMessage()).build();
        }
        return EmailResponse.builder().success(true).data("ok").build();
    }

}

```