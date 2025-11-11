package com.mgdev.om_paye.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.api.url:https://api.brevo.com/v3/smtp/email}")
    private String brevoApiUrl;

    public void sendEmail(String to, String subject, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        String jsonPayload = String.format(
            "{\"sender\":{\"name\":\"OM PAYE\",\"email\":\"noreply@ompaye.com\"}," +
            "\"to\":[{\"email\":\"%s\"}]," +
            "\"subject\":\"%s\"," +
            "\"htmlContent\":\"%s\"}",
            to, subject, body.replace("\n", "<br>")
        );

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                brevoApiUrl, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send email via Brevo: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }

    public void sendOtpEmail(String to, String otpCode) {
        String subject = "Votre code OTP OM PAYE";
        String body = String.format(
            "<html><body>" +
            "<p>Bonjour,</p>" +
            "<p>Votre code de vérification OTP est : <strong>%s</strong></p>" +
            "<p>Ce code expirera dans 5 minutes.</p>" +
            "<p>Cordialement,<br>L'équipe OM PAYE</p>" +
            "</body></html>",
            otpCode
        );
        sendEmail(to, subject, body);
    }
}