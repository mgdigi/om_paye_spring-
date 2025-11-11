package com.mgdev.om_paye.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.event.CompteCreatedEvent;
import com.mgdev.om_paye.service.implementation.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompteCreatedEventListener {

    private final OtpService otpService;

    @EventListener
    @Async
    public void handleCompteCreated(CompteCreatedEvent event) {
        try {
            log.info("Traitement de la création du compte pour l'utilisateur: {}", event.getCompte().getUser().getEmail());

            // Envoyer un OTP de vérification par email
            otpService.sendOtp(event.getCompte().getUser().getEmail());

            log.info("OTP envoyé avec succès pour le compte: {}", event.getCompte().getNumeroCompte());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'OTP pour le compte {}: {}",
                     event.getCompte().getNumeroCompte(), e.getMessage(), e);
        }
    }
}