package com.mgdev.om_paye.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.service.implementation.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs (Admin, Client, Marchand)")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    // @PostMapping
    // @ApiResponse(messageKey = "success.user.created")
    // @Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur (Admin, Client ou Marchand)")
    // public UserResponseDto createUser(@RequestBody UserRequestDto request) {
    //     return userService.createUser(request);
    // }

    // @GetMapping("/{id}")
    // @ApiResponse(messageKey = "success.user.retrieved")
    // @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les détails d'un utilisateur spécifique")
    // public UserResponseDto getUserById(@PathVariable UUID id) {
    //     return userService.getUserById(id);
    // }

    // @GetMapping
    // @ApiResponse(messageKey = "success.users.retrieved")
    // @Operation(summary = "Récupérer tous les utilisateurs", description = "Retourne une liste paginée de tous les utilisateurs")
    // public Page<UserResponseDto> getAllUsers(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(defaultValue = "name") String sortBy,
    //         @RequestParam(defaultValue = "asc") String sortDir) {

    //     Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    //     Pageable pageable = PageRequest.of(page, size, sort);

    //     return userService.getAllUsers(pageable);
    // }

}