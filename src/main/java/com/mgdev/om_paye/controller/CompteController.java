package com.mgdev.om_paye.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse;
import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final CompteRepository compteRepository;


    @PostMapping
    @ApiResponse(messageKey = "compte.created")
    @Operation(summary = "Créer un compte", description = "Crée un nouveau compte bancaire pour un utilisateur")
    public ResponseEntity<CompteResponseDto> createCompte(@RequestBody CompteRequestDto request) {
        CompteResponseDto response = compteService.createCompte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // @GetMapping("/{id}")
    // @ApiResponse(messageKey = "compte.retrieved")
    // @Operation(summary = "Récupérer un compte par ID", description = "Retourne les détails d'un compte spécifique")
    // public ResponseEntity<CompteResponseDto> getCompteById(@PathVariable UUID id) {
    //     CompteResponseDto response = compteService.getCompteById(id);
    //     return ResponseEntity.ok(response);
    // }

    // @GetMapping("/numero/{numeroCompte}")
    // @ApiResponse(messageKey = "compte.retrieved")
    // @Operation(summary = "Récupérer un compte par numéro", description = "Retourne les détails d'un compte via son numéro")
    // public ResponseEntity<CompteResponseDto> getCompteByNumero(@PathVariable String numeroCompte) {
    //     CompteResponseDto response = compteService.getCompteByNumero(numeroCompte);
    //     return ResponseEntity.ok(response);
    // }

    // @GetMapping("/user/{userId}")
    // @ApiResponse(messageKey = "comptes.retrieved")
    // @Operation(summary = "Récupérer les comptes d'un utilisateur", description = "Retourne tous les comptes d'un utilisateur spécifique")
    // public ResponseEntity<List<CompteResponseDto>> getComptesByUserId(@PathVariable UUID userId) {
    //     List<CompteResponseDto> comptes = compteService.getComptesByUserId(userId);
    //     return ResponseEntity.ok(comptes);
    // }

    @GetMapping("/solde")
    @Operation(summary = "Consulter le solde de mon compte", description = "Retourne le solde actuel du compte de l'utilisateur connecté")
    public ResponseEntity<BigDecimal> getSolde() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String subject = authentication.getName();

        // Le subject est maintenant toujours le numéro de téléphone
        User user = userRepository.findByPhoneNumber(subject)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));


        List<Compte> comptes = compteRepository.findByUser(user);
        if (comptes.isEmpty()) {
            throw new IllegalArgumentException("L'utilisateur n'a pas de compte associé");
        }
        Compte compte = comptes.get(0);

        BigDecimal solde = compteService.calculateSolde(compte.getId());
        return ResponseEntity.ok(solde);

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