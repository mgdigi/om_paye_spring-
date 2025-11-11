package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidAdminCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAdminCodeValidator implements ConstraintValidator<ValidAdminCode, String> {

    private static final String ADMIN_CODE_PATTERN = "^ADMIN\\d{4}$";

    @Override
    public boolean isValid(String adminCode, ConstraintValidatorContext context) {
        if (adminCode == null || adminCode.isEmpty()) {
            return false;
        }

        return adminCode.matches(ADMIN_CODE_PATTERN);
    }
}