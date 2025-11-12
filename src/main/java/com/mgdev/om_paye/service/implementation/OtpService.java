package com.mgdev.om_paye.service.implementation;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgdev.om_paye.entity.OtpCode;
import com.mgdev.om_paye.repository.OtpCodeRepository;
import com.mgdev.om_paye.service.EmailService;
import com.mgdev.om_paye.service.SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OtpService.class);

    private final OtpCodeRepository otpCodeRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;

    public void sendOtp(String phoneNumber) {

        otpCodeRepository.markAllAsUsedByPhoneNumber(phoneNumber);

        String otpCode = generateOtpCode();

        logger.info("OTP généré pour le numéro {}: {}", phoneNumber, otpCode);

        OtpCode otp = OtpCode.builder()
                .phoneNumber(phoneNumber)
                .code(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))
                .used(false)
                .build();

        otpCodeRepository.save(otp);

        // Send OTP via SMS using Twilio
        smsService.sendOtpSms(phoneNumber, otpCode);
    }

    public boolean verifyOtp(String phoneNumber, String code) {
        return otpCodeRepository.findByPhoneNumberAndCodeAndUsedFalse(phoneNumber, code)
                .map(otp -> {
                    if (otp.isValid()) {
                        otp.setUsed(true);
                        otpCodeRepository.save(otp);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public boolean hasActiveOtp(String phoneNumber) {
        return otpCodeRepository.existsByPhoneNumberAndUsedFalse(phoneNumber);
    }

    public OtpCode getLatestOtp(String phoneNumber) {
        return otpCodeRepository.findFirstByPhoneNumberAndUsedFalseOrderByExpiresAtDesc(phoneNumber)
                .orElse(null);
    }

    private String generateOtpCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // Nettoyer les OTP expirés toutes les heures
    @Scheduled(fixedRate = 3600000) // 1 heure en millisecondes
    @Transactional
    public void cleanupExpiredOtps() {
        otpCodeRepository.deleteExpiredOtps(LocalDateTime.now());
    }
}