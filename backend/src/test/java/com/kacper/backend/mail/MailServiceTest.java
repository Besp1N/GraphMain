package com.kacper.backend.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Manually inject "fromMail" value using reflection since it's a private field
        setFieldValue(mailService);
    }

    private void setFieldValue(Object targetObject) throws Exception {
        Field field = targetObject.getClass().getDeclaredField("fromMail");

        field.setAccessible(true);

        field.set(targetObject, "noreply@graphmain.com");
    }

    @Test
    void sendsMail() {
        // mail is sent when the email is valid
        String validEmail = "test@example.com";

        mailService.sendMail(validEmail);

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void invalid_emailException() {
        // dont send mail when the email is invalid
        String invalidEmail = "invalid";

        assertThatThrownBy(() -> mailService.sendMail(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Invalid email address: invalid");

        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }


    @Test
    void correctmailset() {
        // correct subject & message in email
        String validEmail = "recipient@test.com";

        mailService.sendMail(validEmail);

        verify(javaMailSender).send((SimpleMailMessage) argThat(mailMessage -> {
            SimpleMailMessage message = (SimpleMailMessage) mailMessage;

            return "GraphMain account created".equals(message.getSubject()) && "You have created your account".equals(message.getText());
        }));
    }


    @Test
    void javaMailSenderException() {
        // javaMailSender exception handled properly
        String validEmail = "test@example.com";

        doThrow(new RuntimeException("Mail sending failed")).when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThatThrownBy(() -> mailService.sendMail(validEmail))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("Mail sending failed");
    }



}
