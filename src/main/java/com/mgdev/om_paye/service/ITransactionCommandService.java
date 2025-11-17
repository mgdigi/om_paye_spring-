package com.mgdev.om_paye.service;

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;

public interface ITransactionCommandService {
    TransactionResponseDto effectuerTransfert(TransfertRequestDto request);
    TransactionResponseDto effectuerPaiement(PaiementRequestDto request);
}