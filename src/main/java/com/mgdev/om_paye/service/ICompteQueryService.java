package com.mgdev.om_paye.service;

import java.util.List;

import com.mgdev.om_paye.dto.response.SoldeResponseDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.enums.TypeTransaction;

public interface ICompteQueryService {
    SoldeResponseDto getSoldeForCurrentUser();
    List<TransactionResponseDto> getTransactionsForCurrentUser(TypeTransaction type);
}