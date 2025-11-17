package com.mgdev.om_paye.service.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.enums.TypeTransaction;
import com.mgdev.om_paye.mapper.TransactionMapper;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.TransactionRepository;
import com.mgdev.om_paye.service.ITransactionQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionQueryService implements ITransactionQueryService {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponseDto> getTransactionsByCompte(UUID compteId) {
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));

        return transactionRepository
                .findByCompteExpediteurOrCompteDestinataire(compte, compte)
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getMyTransactions(TypeTransaction type) {
        
        throw new UnsupportedOperationException("Cette méthode doit être appelée depuis CompteQueryService");
    }
}