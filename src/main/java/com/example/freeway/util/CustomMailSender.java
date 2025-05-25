package com.example.freeway.util;

import com.example.freeway.db.entity.Meeting;
import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.db.entity.SysUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomMailSender {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;


    public void sendMeetingNotification(Meeting meeting, boolean approved, boolean rejected) {
        String subject;
        String statusText;

        if (approved) {
            subject = "Встреча подтверждена";
            statusText = "подтверждена";
        } else if (rejected) {
            subject = "Встреча отклонена";
            statusText = "отклонена";
        } else {
            subject = "Назначена новая встреча";
            statusText = "создана";
        }

        String message = String.format("""
                        Здравствуйте!
                        
                        Встреча между %s %s и %s %s была %s.
                        
                        Тема встречи: %s
                        Время: %s — %s
                        
                        С уважением,
                        Система Freeway
                        """,
                meeting.getStudent().getSecondName(), meeting.getStudent().getName(),
                meeting.getTeacher().getSecondName(), meeting.getTeacher().getName(),
                statusText,
                meeting.getTopic(),
                meeting.getStartTime(),
                meeting.getEndTime()
        );

        send(meeting.getStudent().getEmail(), subject, message);
        send(meeting.getTeacher().getEmail(), subject, message);
    }

    public void sendNewFreeVisitNotificationToTeacher(SysUser teacher, StudentDetails student) {
        String subject = "Новая заявка на свободное посещение";

        String body = String.format("""
                        Здравствуйте, %s!
                        
                        Студент %s %s подал заявку на свободное посещение.
                        
                        Пожалуйста, перейдите в систему Freeway, чтобы согласовать или отклонить заявку.
                        
                        С уважением,  
                        Система Freeway
                        """,
                teacher.getName(),
                student.getUser().getName(),
                student.getUser().getSecondName()
        );

        send(teacher.getEmail(), subject, body);
    }

    public void sendFreeVisitStatusNotification(SysUser student, boolean approved) {
        String subject = "Обновление заявки на свободное посещение";
        String statusText = approved ? "одобрена" : "отклонена";

        String body = String.format("""
                        Здравствуйте, %s %s!
                        
                        Ваша заявка на свободное посещение была %s большинством преподавателей.
                        
                        Вы можете просмотреть детали в ваших заявках.
                        
                        С уважением,
                        Система Freeway
                        """,
                student.getSecondName(), student.getName(),
                statusText
        );

        send(student.getEmail(), subject, body);
    }

    public void send(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, false);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", to);
        } catch (MessagingException | MailException e) {
            log.error("Ошибка при отправке письма на {}: {}", to, e.getMessage(), e);
        }
    }
//    public void sendActivationLink(String mail, UUID code) {
//        try {
//            MimeMessage mimeMessage = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
//
//            helper.setFrom(sender);
//            helper.setTo(mail);
//            helper.setText(String.format(link, code), false);
//            helper.setSubject("Ссылка для активации аккаунта");
//
//            emailSender.send(mimeMessage);
//
//        } catch (MailSendException e) {
//            if (e.getCause() instanceof SendFailedException sfe &&
//                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
//                    smtpEx.getMessage().contains("550")) {
//                throw new EmailSendException("error.user.not_found.email");
//            }
//            throw new EmailSendException("error.mail.send_failed");
//
//        } catch (MessagingException e) {
//            throw new EmailSendException("error.mail.send_failed");
//        }
//    }
//
//    public void sendPasswordResetLink(String mail, String token, Integer passwordLength) throws MessagingException {
//        try {
//            MimeMessage mimeMessage = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
//
//            helper.setFrom(sender);
//            helper.setTo(mail);
//
//            String linkWithParam = String.format(resetLink, token) + "&length=" + passwordLength;
//            helper.setText(linkWithParam, false);
//            helper.setSubject("Ссылка для cброса пароля");
//
//            emailSender.send(mimeMessage);
//
//        } catch (MailSendException e) {
//            if (e.getCause() instanceof SendFailedException sfe &&
//                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
//                    smtpEx.getMessage().contains("550")) {
//                throw new EmailSendException("error.user.not_found.email");
//            }
//            throw new EmailSendException("error.mail.send_failed");
//
//        } catch (MessagingException e) {
//            throw new EmailSendException("error.mail.send_failed");
//        }
//    }

}
