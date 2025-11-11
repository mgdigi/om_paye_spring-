package com.mgdev.om_paye.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.TypeCompte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDto {
    private UUID id;
    private String numeroCompte;
    private String titulaireCompte;
    private BigDecimal solde;  
    private TypeCompte typeCompte;
    private CompteStatus status;
    private UUID userId;
    private String userName;
    private Instant creationDate;
}