package com.mgdev.om_paye.validators;

import org.springframework.stereotype.Component;

import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.enums.TypeCompte;
import com.mgdev.om_paye.repository.CompteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompteValidator {
    private final CompteRepository compteRepository;

    public void validateCompte(CompteRequestDto request) {
        
        try {
            TypeCompte type = TypeCompte.valueOf(request.getTypeCompte().toUpperCase());
            switch (type) {
                case PERSONNEL -> {
                  
                    if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
                        throw new IllegalArgumentException("Le numéro de téléphone est obligatoire pour un compte personnel");
                    }
                }
                case MARCHAND -> {
                 
                    if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
                        throw new IllegalArgumentException("Le numéro de téléphone est obligatoire pour un compte marchand");
                    }
                }
                default -> throw new IllegalArgumentException("Type de compte invalide");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de compte invalide: " + request.getTypeCompte());
        }
    }
}