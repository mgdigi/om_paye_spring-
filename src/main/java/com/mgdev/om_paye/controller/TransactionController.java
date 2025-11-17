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

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.service.ITransactionCommandService;
import com.mgdev.om_paye.service.ITransactionQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Gestion des transactions (transferts et paiements)")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final ITransactionCommandService transactionCommandService;
    private final ITransactionQueryService transactionQueryService;

    @PostMapping("/transfert")
    @com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse(messageKey = "transaction.transfert.created")
    @Operation(
        summary = "Effectuer un transfert",
        description = "Transfère de l'argent du compte de l'utilisateur connecté vers un autre compte. L'expéditeur est automatiquement déterminé par le token JWT."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transfert effectué avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Données invalides ou solde insuffisant"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Destinataire introuvable")
    })
    public ResponseEntity<TransactionResponseDto> effectuerTransfert(
            @Parameter(description = "Détails du transfert", required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransfertRequestDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "destinataireIdentifiant": "+221771234567",
                            "montant": 5000.00,
                            "description": "Paiement de facture"
                        }
                        """)))
            @RequestBody TransfertRequestDto request) {
        TransactionResponseDto response = transactionCommandService.effectuerTransfert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/paiement")
    @com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse(messageKey = "transaction.paiement.created")
    @Operation(
        summary = "Effectuer un paiement",
        description = "Effectue un paiement du compte de l'utilisateur connecté vers un marchand. L'expéditeur est automatiquement déterminé par le token JWT. Le destinataire peut être identifié par numéro de téléphone ou code marchand."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Paiement effectué avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Données invalides ou solde insuffisant"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Marchand introuvable")
    })
    public ResponseEntity<TransactionResponseDto> effectuerPaiement(
            @Parameter(description = "Détails du paiement", required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaiementRequestDto.class),
                    examples = {
                        @ExampleObject(name = "Paiement par numéro de téléphone", value = """
                            {
                                "destinataireIdentifiant": "+221771234567",
                                "montant": 2500.00,
                                "description": "Achat en ligne"
                            }
                            """),
                        @ExampleObject(name = "Paiement par code marchand", value = """
                            {
                                "destinataireIdentifiant": "MARCHAND123",
                                "montant": 15000.00,
                                "description": "Paiement de services"
                            }
                            """)
                    }))
            @RequestBody PaiementRequestDto request) {
        TransactionResponseDto response = transactionCommandService.effectuerPaiement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/compte/{compteId}")
    @Operation(
        summary = "Récupérer les transactions d'un compte",
        description = "Retourne l'historique complet des transactions (transferts et paiements) d'un compte spécifique"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Historique récupéré avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Compte introuvable")
    })
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCompte(
            @Parameter(description = "ID du compte", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID compteId) {
        List<TransactionResponseDto> transactions =
                transactionQueryService.getTransactionsByCompte(compteId);
        return ResponseEntity.ok(transactions);
    }
}