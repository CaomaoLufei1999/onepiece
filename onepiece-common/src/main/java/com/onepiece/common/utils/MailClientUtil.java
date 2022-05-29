package com.onepiece.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @描述 邮件发送客户端工具栏
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-29
 */
@Component
public class MailClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailClientUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public Boolean sendMail(String to, String subject, String content) {
        Boolean result = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            // 发送者
            helper.setFrom(from);
            // 接收者
            helper.setTo(to);
            // 邮件主题
            helper.setSubject(subject);
            // 邮件内容,第二个参数true表示支持html格式
            helper.setText(content, true);

            mailSender.send(helper.getMimeMessage());
            logger.info("发送邮件成功！");
            result = true;
        } catch (MessagingException e) {
            logger.error("发送邮件失败: " + e.getMessage());
        }
        return result;
    }
}
