package com.mgdev.om_paye.dto.response;

import com.mgdev.om_paye.enums.TransactionStatus;
import com.mgdev.om_paye.enums.TypeTransaction;
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
public class TransactionResponseDto {
    private UUID id;
    private String referenceTransaction;
    private BigDecimal montantTransaction;
    private TypeTransaction typeTransaction;
    private TransactionStatus status;
    private String description;
    private BigDecimal frais;
    private String numeroCompteExpediteur;
    private String numeroCompteDestinataire;
    private Instant creationDate;
}