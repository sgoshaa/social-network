package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements SocialNetworkService{

    @Value("${social_network.main_link}")
    private String MAIN_LINK;
    private final Class<EmailService> loggerClass = EmailService.class;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendThis(String htmlMsg, String to, String subject, String method) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setText(htmlMsg, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("Social Network <noreply@socnet.com>");
            mailSender.send(mimeMessage);
            log(loggerClass, LoggerLevel.INFO, method, LoggerValue.EMAIL_SENT, to);
        } catch (MessagingException e) {
            log(loggerClass, LoggerLevel.ERROR, method, LoggerValue.EMAIL_ERROR, e.getMessage());
        }
    }

    public void registerEmail(String to, String subject, String text) {
        String link = MAIN_LINK;
        String htmlMsg = "<body>Your onetime link for registration is " + "<a href=\"" + link + text + "\">HERE!</a> <br>"
                + "Please, use this link to complete your registration.<br>"
                + "Link is confidential! Do not share this  with anyone.</body>"
                + "<body>If it was not YOU, " + "<a href=\"" + link + text + "not_this_email" + "\">follow me</a></body>";
        sendThis(htmlMsg, to, subject, "registerEmail");
    }

    public void sendEmail(String to, String subject, String text) {
        String link = MAIN_LINK + text;
        String htmlMsg = "<a href=\"" + link + "\">Email recovery</a>";
        sendThis(htmlMsg, to, subject, "sendEmail");
    }
}
