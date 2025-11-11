package com.mgdev.om_paye.validators.impl;

import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.validators.annotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true; 
        }

        return !userRepository.existsByEmail(email.trim());
    }
}