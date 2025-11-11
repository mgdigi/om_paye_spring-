package com.mgdev.om_paye.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String otpCode) {
        String subject = "Votre code OTP OM PAYE";
        String body = String.format(
            "Bonjour,\n\n" +
            "Votre code de vérification OTP est : %s\n\n" +
            "Ce code expirera dans 5 minutes.\n\n" +
            "Cordialement,\n" +
            "L'équipe OM PAYE",
            otpCode
        );
        sendEmail(to, subject, body);
    }
}