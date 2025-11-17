package com.mgdev.om_paye.service.implementation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mgdev.om_paye.dto.response.SoldeResponseDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.enums.TypeTransaction;
import com.mgdev.om_paye.exception.UserHasNoAccountException;
import com.mgdev.om_paye.exception.UserNotFoundException;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.service.ICompteQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompteQueryService implements ICompteQueryService {

    private final UserRepository userRepository;
    private final CompteRepository compteRepository;
    private final CompteService compteService;
    private final TransactionService transactionService;

    @Override
    public SoldeResponseDto getSoldeForCurrentUser() {
        User user = getCurrentUser();
        Compte compte = getUserAccount(user);

        BigDecimal solde = compteService.calculateSolde(compte.getId());

        return SoldeResponseDto.builder()
                .nomUtilisateur(user.getName())
                .numeroCompte(compte.getNumeroCompte())
                .solde(solde)
                .build();
    }

    @Override
    public List<TransactionResponseDto> getTransactionsForCurrentUser(TypeTransaction type) {
        User user = getCurrentUser();
        Compte compte = getUserAccount(user);

        return transactionService.getMyTransactions(type);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String subject = authentication.getName();

        return userRepository.findByPhoneNumber(subject)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
    }

    private Compte getUserAccount(User user) {
        List<Compte> comptes = compteRepository.findByUser(user);
        if (comptes.isEmpty()) {
            throw new UserHasNoAccountException("L'utilisateur n'a pas de compte associé");
        }
        return comptes.get(0);
    }
}