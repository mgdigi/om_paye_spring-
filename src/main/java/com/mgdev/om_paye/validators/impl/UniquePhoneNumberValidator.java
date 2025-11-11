package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.validators.annotations.UniquePhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true; 
        }

        return !userRepository.existsByPhoneNumber(phoneNumber.trim());
    }
}