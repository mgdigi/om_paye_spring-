package com.mgdev.om_paye.validators;

import org.springframework.stereotype.Component;

import com.mgdev.om_paye.dto.request.UserRequestDto;
import com.mgdev.om_paye.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class UserValidator {
    private final UserRepository userRepository;

    public void validateUser(UserRequestDto request ){

        
        switch (request.getRole().toUpperCase()) {
            case "CLIENT" -> {
                if (request.getAdresse() == null || request.getAdresse().isBlank())
                    throw new IllegalArgumentException("L’adresse est obligatoire pour un client");
            }
            case "MARCHAND" -> {
                if (request.getBusinessName() == null || request.getBusinessName().isBlank())
                    throw new IllegalArgumentException("Le nom commercial est obligatoire pour un marchand");
            }
            case "ADMIN" -> {
                if (request.getAdminCode() == null || request.getAdminCode().isBlank())
                    throw new IllegalArgumentException("Le code admin est obligatoire pour un admin");
            }
            default -> throw new IllegalArgumentException("Rôle d’utilisateur invalide");
        }

    }
}
