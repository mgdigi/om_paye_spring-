package com.mgdev.om_paye.controller;

import java.math.BigDecimal;
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
import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.service.implementation.CompteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class CompteController {

    private final CompteService compteService;

    @PostMapping
    @ApiResponse(messageKey = "compte.created")
    @Operation(summary = "Créer un compte", description = "Crée un nouveau compte bancaire pour un utilisateur")
    public ResponseEntity<CompteResponseDto> createCompte(@RequestBody CompteRequestDto request) {
        CompteResponseDto response = compteService.createCompte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @ApiResponse(messageKey = "compte.retrieved")
    @Operation(summary = "Récupérer un compte par ID", description = "Retourne les détails d'un compte spécifique")
    public ResponseEntity<CompteResponseDto> getCompteById(@PathVariable UUID id) {
        CompteResponseDto response = compteService.getCompteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroCompte}")
    @ApiResponse(messageKey = "compte.retrieved")
    @Operation(summary = "Récupérer un compte par numéro", description = "Retourne les détails d'un compte via son numéro")
    public ResponseEntity<CompteResponseDto> getCompteByNumero(@PathVariable String numeroCompte) {
        CompteResponseDto response = compteService.getCompteByNumero(numeroCompte);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @ApiResponse(messageKey = "comptes.retrieved")
    @Operation(summary = "Récupérer les comptes d'un utilisateur", description = "Retourne tous les comptes d'un utilisateur spécifique")
    public ResponseEntity<List<CompteResponseDto>> getComptesByUserId(@PathVariable UUID userId) {
        List<CompteResponseDto> comptes = compteService.getComptesByUserId(userId);
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/solde/{compteId}")
    @Operation(summary = "Consulter le solde d'un compte", description = "Retourne le solde actuel d'un compte spécifique")
    public ResponseEntity<BigDecimal> getSolde(@PathVariable UUID compteId) {
        BigDecimal solde = compteService.calculateSolde(compteId);
        return ResponseEntity.ok(solde);
    }
}