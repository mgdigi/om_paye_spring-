package com.mgdev.om_paye.validators;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final CompteRepository compteRepository;
    private final UserRepository userRepository;

    public void validateTransfert(TransfertRequestDto request) {
        // Récupérer l'utilisateur connecté depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhone = authentication.getName();
        User userExpediteur = userRepository.findByPhoneNumber(userPhone)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        List<Compte> comptesExpediteur = compteRepository.findByUser(userExpediteur);
        if (comptesExpediteur.isEmpty()) {
            throw new IllegalArgumentException("L'utilisateur n'a pas de compte associé");
        }
        Compte compteExpediteur = comptesExpediteur.get(0);

        if (compteExpediteur.getSolde().compareTo(request.getMontant()) < 0) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer le transfert");
        }

        boolean destinataireExists = compteRepository.existsByNumeroCompte(request.getDestinataireIdentifiant()) ||
                                   compteRepository.existsByUser_Email(request.getDestinataireIdentifiant()) ||
                                   compteRepository.existsByUser_PhoneNumber(request.getDestinataireIdentifiant());

        if (!destinataireExists) {
            throw new IllegalArgumentException("Destinataire introuvable");
        }

        if (compteExpediteur.getUser().getEmail().equals(request.getDestinataireIdentifiant()) ||
            compteExpediteur.getUser().getPhoneNumber().equals(request.getDestinataireIdentifiant())) {
            throw new IllegalArgumentException("Impossible de transférer de l'argent vers son propre compte");
        }
    }

    public void validatePaiement(PaiementRequestDto request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhone = authentication.getName();
        User userClient = userRepository.findByPhoneNumber(userPhone)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

       
        List<Compte> comptesClient = compteRepository.findByUser(userClient);
        if (comptesClient.isEmpty()) {
            throw new IllegalArgumentException("L'utilisateur n'a pas de compte associé");
        }
        Compte compteClient = comptesClient.get(0);

        
        if (compteClient.getSolde().compareTo(request.getMontant()) < 0) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer le paiement");
        }

      
        boolean marchandExists = userRepository.findAll().stream()
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .map(user -> (com.mgdev.om_paye.entity.Marchand) user)
                .anyMatch(m -> request.getDestinataireIdentifiant().equals(m.getCodeMarchand())) ||
                userRepository.findByPhoneNumber(request.getDestinataireIdentifiant())
                    .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                    .isPresent();

        if (!marchandExists) {
            throw new IllegalArgumentException("Marchand introuvable");
        }
    }
}