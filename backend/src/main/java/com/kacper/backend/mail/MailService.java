package com.kacper.backend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Mail service class
 *
 * @Author Kacper Karabinowski
 */
@Service
public class MailService
{
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * @param javaMailSender java mail sender
     */
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * @param to email address
     * @throws IllegalArgumentException if email is invalid
     */
    public void sendMail(String to) {
        if (!isValidEmail(to)) {
            throw new IllegalArgumentException("Invalid email address: " + to);
        }

        MailStructure mailStructure = MailStructure.builder()
                .message("You have created your account. If it's not you, ignore it.")
                .subject("GraphMain account created")
                .build();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(fromMail);
        mailMessage.setSubject(mailStructure.subject());
        mailMessage.setText(mailStructure.message());

        javaMailSender.send(mailMessage);
    }

    public void sendNotificationMail(String to, String Notification) {
        MailStructure mailStructure = MailStructure.builder()
                .message(Notification)
                .subject("Graph Main notification")
                .build();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(fromMail);
        mailMessage.setSubject(mailStructure.subject());
        mailMessage.setText(mailStructure.message());

        javaMailSender.send(mailMessage);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }
}