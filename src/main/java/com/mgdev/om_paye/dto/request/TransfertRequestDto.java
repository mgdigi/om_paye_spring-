package com.mgdev.om_paye.dto.request;

import java.math.BigDecimal;

import com.mgdev.om_paye.validators.annotations.NotBlank;
import com.mgdev.om_paye.validators.annotations.ValidAccountNumber;
import com.mgdev.om_paye.validators.annotations.ValidAmount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransfertRequestDto {

    @NotBlank
    @ValidAccountNumber
    private String numeroCompteExpediteur;

    @NotBlank
    private String destinataireIdentifiant;

    @ValidAmount
    private BigDecimal montant;

    private String description;
}