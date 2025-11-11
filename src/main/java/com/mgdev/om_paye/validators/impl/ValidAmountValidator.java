package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidAmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("100");

    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
        if (amount == null) {
            return false;
        }

        return amount.compareTo(MIN_AMOUNT) >= 0;
    }
}