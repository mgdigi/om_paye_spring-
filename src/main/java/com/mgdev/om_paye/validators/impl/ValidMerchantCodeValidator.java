package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidMerchantCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidMerchantCodeValidator implements ConstraintValidator<ValidMerchantCode, String> {

    private static final String MERCHANT_CODE_PATTERN = "^MARCHAND\\d{3}$";

    @Override
    public boolean isValid(String merchantCode, ConstraintValidatorContext context) {
        if (merchantCode == null || merchantCode.isEmpty()) {
            return false;
        }

        return merchantCode.matches(MERCHANT_CODE_PATTERN);
    }
}