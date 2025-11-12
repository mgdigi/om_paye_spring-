package com.mgdev.om_paye.dto.response;

import java.util.UUID;

import com.mgdev.om_paye.enums.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthResponse {

    private String token;
    private String refreshToken;
    private UUID userId;
    private UserRole role;

}
