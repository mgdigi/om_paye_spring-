package com.mgdev.om_paye.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse;
import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.dto.response.SoldeResponseDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.enums.TypeTransaction;
import com.mgdev.om_paye.service.ICompteQueryService;
import com.mgdev.om_paye.service.implementation.CompteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class CompteController {

    private final CompteService compteService;
    private final ICompteQueryService compteQueryService;

    @PostMapping
    @ApiResponse(messageKey = "compte.created")
    @Operation(summary = "Créer un compte", description = "Crée un nouveau compte bancaire pour un utilisateur")
    public ResponseEntity<CompteResponseDto> createCompte(@RequestBody CompteRequestDto request) {
        CompteResponseDto response = compteService.createCompte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/solde")
    @Operation(summary = "Consulter le solde de mon compte", description = "Retourne le solde actuel du compte de l'utilisateur connecté avec ses informations")
    public ResponseEntity<SoldeResponseDto> getSolde() {
        SoldeResponseDto response = compteQueryService.getSoldeForCurrentUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    @Operation(summary = "Récupérer mes transactions", description = "Retourne l'historique des transactions du compte de l'utilisateur connecté avec possibilité de filtrer par type")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Historique récupéré avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Utilisateur sans compte")
    })
    public ResponseEntity<List<TransactionResponseDto>> getMyTransactions(
            @Parameter(description = "Type de transaction pour filtrer (optionnel)", example = "TRANSFERT")
            @RequestParam(required = false) TypeTransaction type) {
        List<TransactionResponseDto> transactions = compteQueryService.getTransactionsForCurrentUser(type);
        return ResponseEntity.ok(transactions);
    }
}