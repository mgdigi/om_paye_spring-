package com.mgdev.om_paye.service;

import java.util.List;
import java.util.UUID;

import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.enums.TypeTransaction;

public interface ITransactionQueryService {
    List<TransactionResponseDto> getTransactionsByCompte(UUID compteId);
    List<TransactionResponseDto> getMyTransactions(TypeTransaction type);
}