package com.mgdev.om_paye.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.mgdev.om_paye.enums.UserRole;
import com.mgdev.om_paye.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    
    private UUID id;
    private String name;
    private String email;
    private String nci;
    private String phoneNumber;
    private UserStatus status;
    private UserRole role;
    private Instant creationDate;
}