package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidCni;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCniValidator implements ConstraintValidator<ValidCni, String> {

    @Override
    public boolean isValid(String cni, ConstraintValidatorContext context) {
        if (cni == null || cni.isEmpty()) {
            return false;
        }

        String normalized = cni.trim();

        for (char c : normalized.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return normalized.length() >= 12 && normalized.length() <= 14;
    }
}
