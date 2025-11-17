package com.mgdev.om_paye.service.implementation;

import java.math.BigDecimal;
import java.util.UUID;

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
import com.mgdev.om_paye.exception.InsufficientBalanceException;
import com.mgdev.om_paye.exception.RecipientNotFoundException;
import com.mgdev.om_paye.exception.UserHasNoAccountException;
import com.mgdev.om_paye.exception.UserNotFoundException;
import com.mgdev.om_paye.mapper.TransactionMapper;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.TransactionRepository;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.service.ITransactionCommandService;
import com.mgdev.om_paye.validators.TransactionValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionCommandService implements ITransactionCommandService {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    private final UserRepository userRepository;
    private final CompteService compteService;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    @Override
    public TransactionResponseDto effectuerTransfert(TransfertRequestDto request) {
        transactionValidator.validateTransfert(request);

        User userExpediteur = getCurrentUser();
        Compte compteExpediteur = getUserAccount(userExpediteur);
        Compte compteDestinataire = findCompteByIdentifiant(request.getDestinataireIdentifiant());

        BigDecimal soldeActuel = compteService.calculateSolde(compteExpediteur.getId());
        if (soldeActuel.compareTo(request.getMontant()) < 0) {
            throw new InsufficientBalanceException(soldeActuel, request.getMontant());
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

    @Override
    public TransactionResponseDto effectuerPaiement(PaiementRequestDto request) {
        transactionValidator.validatePaiement(request);

        User userClient = getCurrentUser();
        Compte compteClient = getUserAccount(userClient);
        Compte compteMarchand = findMarchandByIdentifiant(request.getDestinataireIdentifiant());

        BigDecimal soldeActuel = compteService.calculateSolde(compteClient.getId());
        if (soldeActuel.compareTo(request.getMontant()) < 0) {
            throw new InsufficientBalanceException(soldeActuel, request.getMontant());
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String subject = authentication.getName();
        return userRepository.findByPhoneNumber(subject)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
    }

    private Compte getUserAccount(User user) {
        return compteRepository.findByUser(user).stream()
                .findFirst()
                .orElseThrow(() -> new UserHasNoAccountException("L'utilisateur n'a pas de compte associé"));
    }

    private Compte findCompteByIdentifiant(String identifiant) {
        return compteRepository.findByNumeroCompte(identifiant)
                .orElseGet(() -> {
                    User user = userRepository.findByPhoneNumber(identifiant)
                            .orElseThrow(() -> new RecipientNotFoundException("Destinataire introuvable"));
                    return compteRepository.findByUser(user).stream()
                            .findFirst()
                            .orElseThrow(() -> new RecipientNotFoundException("Le destinataire n'a pas de compte"));
                });
    }

    private String generateReference() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Compte findMarchandByIdentifiant(String identifiant) {
        User marchand = userRepository.findAll().stream()
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .map(user -> (com.mgdev.om_paye.entity.Marchand) user)
                .filter(m -> identifiant.equals(m.getCodeMarchand()))
                .findFirst()
                .orElse(null);

        if (marchand != null) {
            return compteRepository.findByUser(marchand).stream()
                    .findFirst()
                    .orElseThrow(() -> new RecipientNotFoundException("Le marchand n'a pas de compte associé"));
        }

        marchand = userRepository.findByPhoneNumber(identifiant)
                .filter(user -> user instanceof com.mgdev.om_paye.entity.Marchand)
                .orElseThrow(() -> new RecipientNotFoundException("Marchand introuvable avec cet identifiant"));

        return compteRepository.findByUser(marchand).stream()
                .findFirst()
                .orElseThrow(() -> new RecipientNotFoundException("Le marchand n'a pas de compte associé"));
    }
}