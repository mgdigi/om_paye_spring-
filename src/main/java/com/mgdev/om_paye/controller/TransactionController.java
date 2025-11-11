package com.mgdev.om_paye.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse;
import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.service.implementation.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfert")
    @ApiResponse(messageKey = "transaction.transfert.created")
    public ResponseEntity<TransactionResponseDto> effectuerTransfert(
            @RequestBody TransfertRequestDto request) {
        TransactionResponseDto response = transactionService.effectuerTransfert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/paiement")
    @ApiResponse(messageKey = "transaction.paiement.created")
    public ResponseEntity<TransactionResponseDto> effectuerPaiement(
            @RequestBody PaiementRequestDto request) {
        TransactionResponseDto response = transactionService.effectuerPaiement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/compte/{compteId}")
    @ApiResponse(messageKey = "transactions.retrieved")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCompte(
            @PathVariable UUID compteId) {
        List<TransactionResponseDto> transactions =
                transactionService.getTransactionsByCompte(compteId);
        return ResponseEntity.ok(transactions);
    }
}