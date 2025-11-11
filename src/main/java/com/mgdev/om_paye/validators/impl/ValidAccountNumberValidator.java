package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidAccountNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAccountNumberValidator implements ConstraintValidator<ValidAccountNumber, String> {

    @Override
    public boolean isValid(String accountNumber, ConstraintValidatorContext context) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return false;
        }

        String normalized = accountNumber.trim();

        // Vérifier que ça commence par 221 et fait exactement 12 caractères
        if (!normalized.startsWith("221") || normalized.length() != 12) {
            return false;
        }

        // Vérifier que tous les caractères sont des chiffres
        for (char c : normalized.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
}