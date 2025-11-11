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
import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.service.implementation.CompteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor

public class CompteController {

    private final CompteService compteService;


    @PostMapping
    @ApiResponse(messageKey = "compte.created")
    public ResponseEntity<CompteResponseDto> createCompte(@RequestBody CompteRequestDto request) {
        CompteResponseDto response = compteService.createCompte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @ApiResponse(messageKey = "compte.retrieved")
    public ResponseEntity<CompteResponseDto> getCompteById(@PathVariable UUID id) {
        CompteResponseDto response = compteService.getCompteById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numeroCompte}")
    @ApiResponse(messageKey = "compte.retrieved")
    public ResponseEntity<CompteResponseDto> getCompteByNumero(@PathVariable String numeroCompte) {
        CompteResponseDto response = compteService.getCompteByNumero(numeroCompte);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @ApiResponse(messageKey = "comptes.retrieved")
    public ResponseEntity<List<CompteResponseDto>> getComptesByUserId(@PathVariable UUID userId) {
        List<CompteResponseDto> comptes = compteService.getComptesByUserId(userId);
        return ResponseEntity.ok(comptes);
    }
}