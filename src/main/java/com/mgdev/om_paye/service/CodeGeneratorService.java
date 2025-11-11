package com.mgdev.om_paye.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeGeneratorService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ADMIN_PREFIX = "ADMIN";
    private static final String MERCHANT_PREFIX = "MARCHAND";

    /**
     * Génère un code admin unique au format ADMINXXXX
     */
    public String generateAdminCode() {
        int randomNumber = 1000 + RANDOM.nextInt(9000); // Génère un nombre entre 1000 et 9999
        return ADMIN_PREFIX + randomNumber;
    }

    /**
     * Génère un code marchand unique au format MARCHANDXXX
     */
    public String generateMerchantCode() {
        int randomNumber = 100 + RANDOM.nextInt(900); // Génère un nombre entre 100 et 999
        return MERCHANT_PREFIX + randomNumber;
    }

    /**
     * Vérifie si un code admin est valide
     */
    public boolean isValidAdminCode(String code) {
        return code != null && code.matches("^" + ADMIN_PREFIX + "\\d{4}$");
    }

    /**
     * Vérifie si un code marchand est valide
     */
    public boolean isValidMerchantCode(String code) {
        return code != null && code.matches("^" + MERCHANT_PREFIX + "\\d{3}$");
    }
}