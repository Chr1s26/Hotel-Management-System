package com.project.HotelManagementSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Hotel Management System - OTP");
        helper.setFrom("htetkyawswarlinn44@gmail.com");

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("websiteUrl", "www.hotelManagementSystem.org");

        String htmlContent = templateEngine.process("otp-mail-template", context);
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    @Async
    public void sendMailWithAttachment(String recipicent, String subject, byte[] bytes, String zipFileName) throws MessagingException {
       MimeMessage mimeMessage = mailSender.createMimeMessage();
       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
       helper.setTo(recipicent);
       helper.setSubject(subject);
       helper.setFrom("htetkyawswarlinn44@gmail.com");
       Context context = new Context();
       context.setVariable("subject", subject);
       context.setVariable("senderEmail", "htetkyawswarlinn44@gmail.com");
       String htmlContent = templateEngine.process("exports/export-email-template", context);
       helper.setText(htmlContent, true);
       ByteArrayResource attachment = new ByteArrayResource(bytes);
       helper.addAttachment(zipFileName, attachment);
       mailSender.send(mimeMessage);
    }
}
