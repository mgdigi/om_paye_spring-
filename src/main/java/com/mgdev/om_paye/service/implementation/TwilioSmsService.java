package com.mgdev.om_paye.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mgdev.om_paye.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TwilioSmsService implements SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio initialized with account SID: {}", accountSid);
    }

    @Override
    public void sendOtpSms(String phoneNumber, String otpCode) {
        try {
            // Format the phone number to international format if needed
            String formattedPhoneNumber = formatPhoneNumber(phoneNumber);

            Message message = Message.creator(
                new PhoneNumber(formattedPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Votre code de vérification OM PAYE est: " + otpCode + ". Ce code expire dans 5 minutes."
            ).create();

            log.info("OTP SMS sent successfully to {} with SID: {}", phoneNumber, message.getSid());
        } catch (Exception e) {
            log.error("Failed to send OTP SMS to {}: {}", phoneNumber, e.getMessage(), e);

            throw new RuntimeException("Erreur lors de l'envoi du SMS OTP", e);

        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Remove any spaces, dashes, or other non-numeric characters
        String cleaned = phoneNumber.replaceAll("[^\\d]", "");

        // If it starts with 0, replace with +221 (Senegal country code)
        if (cleaned.startsWith("0")) {
            return "+221" + cleaned.substring(1);
        }

        // If it doesn't start with +, add Senegal country code
        if (!cleaned.startsWith("+")) {
            return "+221" + cleaned;
        }

        return cleaned;
    }
}