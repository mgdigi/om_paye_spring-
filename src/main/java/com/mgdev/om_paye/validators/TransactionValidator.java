package com.mgdev.om_paye.validators;

import org.springframework.stereotype.Component;

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.repository.CompteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final CompteRepository compteRepository;

    public void validateTransfert(TransfertRequestDto request) {
        
        Compte compteExpediteur = compteRepository.findByNumeroCompte(request.getNumeroCompteExpediteur())
            .orElseThrow(() -> new IllegalArgumentException("Compte expéditeur introuvable"));

       
        if (compteExpediteur.getSolde().compareTo(request.getMontant()) < 0) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer le transfert");
        }

        
        boolean destinataireExists = compteRepository.existsByNumeroCompte(request.getDestinataireIdentifiant()) ||
                                   compteRepository.existsByUser_Email(request.getDestinataireIdentifiant()) ||
                                   compteRepository.existsByUser_PhoneNumber(request.getDestinataireIdentifiant());

        if (!destinataireExists) {
            throw new IllegalArgumentException("Destinataire introuvable");
        }

        
        if (request.getNumeroCompteExpediteur().equals(request.getDestinataireIdentifiant()) ||
            compteExpediteur.getUser().getEmail().equals(request.getDestinataireIdentifiant()) ||
            compteExpediteur.getUser().getPhoneNumber().equals(request.getDestinataireIdentifiant())) {
            throw new IllegalArgumentException("Impossible de transférer de l'argent vers son propre compte");
        }
    }

    public void validatePaiement(PaiementRequestDto request) {
      
        Compte compteClient = compteRepository.findByNumeroCompte(request.getNumeroCompteClient())
            .orElseThrow(() -> new IllegalArgumentException("Compte client introuvable"));

       
        if (compteClient.getSolde().compareTo(request.getMontant()) < 0) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer le paiement");
        }

        
        boolean marchandExists = compteRepository.existsByCodeMarchand(request.getCodeMarchand());
        if (!marchandExists) {
            throw new IllegalArgumentException("Marchand introuvable");
        }
    }
}