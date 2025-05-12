package com.example.freeway.util;

import com.example.freeway.exception.EmailSendException;
import com.sun.mail.smtp.SMTPAddressFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSender {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Value("${activation.link}")
    private String link;

    @Value("${reset.password.link}")
    private String resetLink;

    public void sendActivationLink(String mail, UUID code) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setFrom(sender);
            helper.setTo(mail);
            helper.setText(String.format(link, code), false);
            helper.setSubject("Ссылка для активации личного кабинета");

            emailSender.send(mimeMessage);

        } catch (MailSendException e) {
            if (e.getCause() instanceof SendFailedException sfe &&
                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
                    smtpEx.getMessage().contains("550")) {
                throw new EmailSendException("error.user.not_found.email");
            }
            throw new EmailSendException("error.mail.send_failed");

        } catch (MessagingException e) {
            throw new EmailSendException("error.mail.send_failed");
        }
    }

    public void sendPasswordResetLink(String mail, String token, Integer passwordLength) throws MessagingException {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setFrom(sender);
            helper.setTo(mail);

            String linkWithParam = String.format(resetLink, token) + "&length=" + passwordLength;
            helper.setText(linkWithParam, false);
            helper.setSubject("Ссылка для cброса пароля личного кабинета");

            emailSender.send(mimeMessage);

        } catch (MailSendException e) {
            if (e.getCause() instanceof SendFailedException sfe &&
                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
                    smtpEx.getMessage().contains("550")) {
                throw new EmailSendException("error.user.not_found.email");
            }
            throw new EmailSendException("error.mail.send_failed");

        } catch (MessagingException e) {
            throw new EmailSendException("error.mail.send_failed");
        }
    }

}
