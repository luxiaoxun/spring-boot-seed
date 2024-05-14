package com.luxx.seed.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
@Slf4j
public class MailService {
    @Value("${mail.host:smtp.feishu.cn}")
    private String mailHost;

    @Value("${mail.port:587}")
    private Integer mailPort;

    @Value("${mail.ssl.enable:false}")
    private Boolean mailSslEnable;

    @Value("${mail.starttls.enable:false}")
    private Boolean mailStarttlsEnable;

    @Value("${mail.auth.enable:false}")
    private Boolean mailAuthEnable;

    @Value("${mail.auth.username:your-email@example.com}")
    private String mailAuthUsername;

    @Value("${mail.auth.password:your-password}")
    private String mailAuthPassword;

    public boolean sendMail(String from, String to, String subject, String text) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailHost);
        properties.put("mail.smtp.port", mailPort);
        properties.put("mail.smtp.ssl.enable", mailSslEnable);
        properties.put("mail.smtp.starttls.enable", mailStarttlsEnable);
        properties.put("mail.smtp.auth", mailAuthEnable);

        try {
            Session session = Session.getDefaultInstance(properties);
            if (mailAuthEnable) {
                session = Session.getInstance(properties, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailAuthUsername, mailAuthPassword);
                    }
                });
            }

            MimeMessage message = new MimeMessage(session);
            //发件人
            message.setFrom(new InternetAddress(from));
            //支持多个收件人："recipient1@example.com,recipient2@example.com"
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            //邮件标题
            message.setSubject(subject);
            //邮件正文
            message.setText(text);
            //发送邮件
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            log.error("Send email error: " + ex);
            return false;
        }
    }

}
