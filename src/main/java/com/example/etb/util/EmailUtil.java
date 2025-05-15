package com.example.etb.util;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LogUtil logUtil;

    /**
     * Sends a simple email (text only)
     *
     * @param to      Recipient email address
     * @param subject Email subject
     * @param body    Email body (plain text)
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false means plain text

            mailSender.send(message);
            logUtil.logSuccess("Mails", "Simple email sent successfully to {}");
        } catch (MessagingException e) {
            logUtil.logError("Mails", "Simple email sent failed");
        }
    }

    /**
     * Sends an email with an HTML body
     *
     * @param to       Recipient email address
     * @param subject  Email subject
     * @param htmlBody Email body (HTML content)
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML content

            mailSender.send(message);
            logUtil.logSuccess("Mails", "HTML email sent successfully to {}");
        } catch (MessagingException e) {
            logUtil.logError("Mails", "HTML email sent failed");
        }
    }

    /**
     * Sends an email with an attachment
     *
     * @param to             Recipient email address
     * @param subject        Email subject
     * @param body           Email body (plain text)
     * @param attachmentPath File path of the attachment
     */
    public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false means plain text

            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);
            logUtil.logSuccess("Mails", "Email with attachment sent successfully to {}");

        } catch (MessagingException e) {
            logUtil.logError("Mails", "Email with attachment sent failed");
        }
    }
}
