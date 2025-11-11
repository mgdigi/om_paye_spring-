package com.mgdev.om_paye.dto.response;

import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDto {
    private UUID id;
    private String numeroCompte;
    private String titulaireCompte;
    private BigDecimal solde;  
    private String codeMarchand;  
    private TypeCompte typeCompte;
    private CompteStatus status;
    private UUID userId;
    private String userName;
    private Instant creationDate;
}