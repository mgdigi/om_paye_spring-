package com.mgdev.om_paye.service.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.Transaction;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.enums.TransactionStatus;
import com.mgdev.om_paye.enums.TypeTransaction;
import com.mgdev.om_paye.mapper.TransactionMapper;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.TransactionRepository;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.validators.TransactionValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    private final UserRepository userRepository;
    private final CompteService compteService;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    public TransactionResponseDto effectuerTransfert(TransfertRequestDto request) {
        // Validation des données d'entrée
        transactionValidator.validateTransfert(request);

        // Récupérer l'utilisateur connecté depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhone = authentication.getName();
        User userExpediteur = userRepository.findByPhoneNumber(userPhone)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Récupérer le compte de l'utilisateur connecté (premier compte trouvé)
        List<Compte> comptesExpediteur = compteRepository.findByUser(userExpediteur);
        if (comptesExpediteur.isEmpty()) {
            throw new IllegalArgumentException("L'utilisateur n'a pas de compte associé");
        }
        Compte compteExpediteur = comptesExpediteur.get(0);

        Compte compteDestinataire = findCompteByIdentifiant(request.getDestinataireIdentifiant());

        BigDecimal soldeActuel = compteService.calculateSolde(compteExpediteur.getId());
        if (soldeActuel.compareTo(request.getMontant()) < 0) {
            throw new IllegalStateException("Solde insuffisant. Solde actuel: " + soldeActuel);
        }

        Transaction transaction = transactionMapper.transfertToEntity(request);
        transaction.setReferenceTransaction(generateReference());
        transaction.setTypeTransaction(TypeTransaction.TRANSFERT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setFrais(BigDecimal.ZERO);
        transaction.setCompteExpediteur(compteExpediteur);
        transaction.setCompteDestinataire(compteDestinataire);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public TransactionResponseDto effectuerPaiement(PaiementRequestDto request) {

        transactionValidator.validatePaiement(request);

        // Récupérer l'utilisateur connecté depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userPhone = authentication.getName();
        User userClient = userRepository.findByPhoneNumber(userPhone)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));


        // Récupérer le compte de l'utilisateur connecté (premier compte trouvé)
        List<Compte> comptesClient = compteRepository.findByUser(userClient);
        if (comptesClient.isEmpty()) {
            throw new IllegalArgumentException("L'utilisateur n'a pas de compte associé");
        }
        Compte compteClient = comptesClient.get(0);

        // Recherche du marchand par codeMarchand ou numéro de téléphone
        Compte compteMarchand = findMarchandByIdentifiant(request.getDestinataireIdentifiant());

        // Recherche du marchand par codeMarchand dans la table User (Marchand)
        // Pour l'instant, on utilise une approche simple - on peut améliorer avec une requête personnalisée
        User marchand = userRepository.findAll().stream()
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .map(user -> (com.mgdev.om_paye.entity.Marchand) user)
                .filter(m -> request.getCodeMarchand().equals(m.getCodeMarchand()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Code marchand invalide"));


        // Récupération du compte du marchand (premier compte trouvé)
        List<Compte> comptesMarchand = compteRepository.findByUser(marchand);
        if (comptesMarchand.isEmpty()) {
            throw new IllegalArgumentException("Le marchand n'a pas de compte associé");
        }
        Compte compteMarchand = comptesMarchand.get(0);

        BigDecimal soldeActuel = compteService.calculateSolde(compteClient.getId());
        if (soldeActuel.compareTo(request.getMontant()) < 0) {
            throw new IllegalStateException("Solde insuffisant. Solde actuel: " + soldeActuel);
        }

        Transaction transaction = transactionMapper.paiementToEntity(request);
        transaction.setReferenceTransaction(generateReference());
        transaction.setTypeTransaction(TypeTransaction.PAIEMENT_MARCHAND);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setFrais(BigDecimal.ZERO);
        transaction.setCompteExpediteur(compteClient);
        transaction.setCompteDestinataire(compteMarchand);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

  
    public List<TransactionResponseDto> getTransactionsByCompte(UUID compteId) {
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));

        return transactionRepository
                .findByCompteExpediteurOrCompteDestinataire(compte, compte)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

   
    private Compte findCompteByIdentifiant(String identifiant) {
        
        return compteRepository.findByNumeroCompte(identifiant)
                .orElseGet(() -> {
                   
                    User user = userRepository.findByPhoneNumber(identifiant)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Aucun compte trouvé avec cet identifiant"));
                    
                    List<Compte> comptes = compteRepository.findByUser(user);
                    if (comptes.isEmpty()) {
                        throw new IllegalArgumentException("L'utilisateur n'a pas de compte");
                    }
                    return comptes.get(0);  
                });
    }

  
    private String generateReference() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private TransactionResponseDto mapToResponseDto(Transaction transaction) {
        TransactionResponseDto dto = transactionMapper.toDto(transaction);
        return dto;
    }

    
    private Compte findMarchandByIdentifiant(String identifiant) {
       
        User marchand = userRepository.findAll().stream()
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .map(user -> (com.mgdev.om_paye.entity.Marchand) user)
                .filter(m -> identifiant.equals(m.getCodeMarchand()))
                .findFirst()
                .orElse(null);

        if (marchand != null) {
            List<Compte> comptes = compteRepository.findByUser(marchand);
            if (!comptes.isEmpty()) {
                return comptes.get(0);
            }
        }

      
        marchand = userRepository.findByPhoneNumber(identifiant)
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .orElseThrow(() -> new IllegalArgumentException("Marchand introuvable avec cet identifiant"));

        List<Compte> comptes = compteRepository.findByUser(marchand);
        if (comptes.isEmpty()) {
            throw new IllegalArgumentException("Le marchand n'a pas de compte associé");
        }
        return comptes.get(0);
    }
}