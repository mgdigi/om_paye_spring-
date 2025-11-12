package com.mgdev.om_paye.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.dto.request.LoginRequestDto;
import com.mgdev.om_paye.dto.response.AuthResponse;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des tokens")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne les tokens JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto request) {
        User user;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
         
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        } else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            
            user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        } else {
            throw new IllegalArgumentException("Email ou numéro de téléphone requis");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe invalide");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());

      
        String subject = user.getPhoneNumber();

        String accessToken = jwtService.generateToken(subject, claims);
        String refreshToken = jwtService.generateRefreshToken(subject);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .userId(user.getId())
                        .role(user.getRole())
                        .build()
        );
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rafraîchir le token", description = "Génère un nouveau token d'accès à partir du refresh token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String username = jwtService.extractUsername(refreshToken);

        if (!jwtService.isTokenValid(refreshToken, username)) {
            throw new IllegalArgumentException("Token invalide ou expiré");
        }

        String newAccessToken = jwtService.generateToken(username, new HashMap<>());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(newAccessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }
}
