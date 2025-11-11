package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.validators.annotations.ValidSenegalPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSenegalPhoneValidator implements ConstraintValidator<ValidSenegalPhone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        
        String normalized = phone.replaceAll("\\s+", "");
        if (normalized.startsWith("+221")) {
            normalized = normalized.substring(4);
        } else if (normalized.startsWith("00221")) {
            normalized = normalized.substring(5);
        }

    
        if (normalized.length() != 9) {
            return false;
        }

       
        String prefix = normalized.substring(0, 2);
        return switch (prefix) {
            case "77", "78", "76", "70", "75" -> true;
            default -> false;
        };
    }
}
