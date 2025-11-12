package com.mgdev.om_paye.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.dto.request.OtpVerificationRequestDto;
import com.mgdev.om_paye.dto.response.AuthResponse;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.UserStatus;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.security.JwtService;
import com.mgdev.om_paye.service.implementation.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
@Tag(name = "OTP", description = "Gestion des codes de vérification OTP")
public class OtpController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final CompteRepository compteRepository;
    private final JwtService jwtService;

    @PostMapping("/verify")
    @Operation(summary = "Vérifier un code OTP", description = "Vérifie la validité d'un code OTP pour un numéro de téléphone donné et active le compte")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpVerificationRequestDto request) {
        boolean isValid = otpService.verifyOtp(request.getPhoneNumber(), request.getCode());

        if (isValid) {
            // Activer l'utilisateur et son compte
            User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            // Activer tous les comptes de l'utilisateur
            for (Compte compte : user.getComptes()) {
                compte.setStatus(CompteStatus.ACTIVE);
                compteRepository.save(compte);
            }

            // Générer le token JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("userId", user.getId());

            String token = jwtService.generateToken(user.getPhoneNumber(), claims);
            String refreshToken = jwtService.generateRefreshToken(user.getPhoneNumber());

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .role(user.getRole())
                    .build();

            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}