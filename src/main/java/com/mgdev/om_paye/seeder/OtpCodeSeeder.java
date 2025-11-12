package com.mgdev.om_paye.seeder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.entity.OtpCode;
import com.mgdev.om_paye.repository.OtpCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class OtpCodeSeeder implements CommandLineRunner {

    private final OtpCodeRepository otpCodeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (otpCodeRepository.count() > 0) {
            log.info("OTP codes already seeded, skipping...");
            return;
        }

        log.info("Seeding OTP codes...");

        // Créer quelques codes OTP de test
        List<OtpCode> otpCodes = Arrays.asList(
            OtpCode.builder()
                .phoneNumber("+221771234567")
                .code("123456")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build(),
            OtpCode.builder()
                .phoneNumber("+221782345678")
                .code("654321")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build(),
            OtpCode.builder()
                .phoneNumber("+221793456789")
                .code("111111")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build(),
            // Code OTP expiré pour test
            OtpCode.builder()
                .phoneNumber("+221704567890")
                .code("000000")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .used(false)
                .build(),
            // Code OTP déjà utilisé
            OtpCode.builder()
                .phoneNumber("+221715678901")
                .code("999999")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(true)
                .build()
        );

        // Sauvegarder tous les codes OTP
        otpCodeRepository.saveAll(otpCodes);

        log.info("OTP codes seeded successfully:");
        log.info("- {} OTP codes créés", otpCodes.size());

        // Afficher les détails des codes OTP valides
        otpCodes.stream()
            .filter(OtpCode::isValid)
            .forEach(otp ->
                log.info("OTP valide: {} - Code: {} - Expire: {}",
                    otp.getPhoneNumber(), otp.getCode(), otp.getExpiresAt())
            );
    }
}