package com.mgdev.om_paye.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse;
import com.mgdev.om_paye.dto.request.OtpVerificationRequestDto;
import com.mgdev.om_paye.service.implementation.OtpService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/verify")
    @ApiResponse(messageKey = "otp.verified")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequestDto request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getCode());

        if (isValid) {
            return ResponseEntity.ok("Code OTP vérifié avec succès");
        } else {
            return ResponseEntity.badRequest().body("Code OTP invalide ou expiré");
        }
    }
}