package com.mgdev.om_paye.dto.request;

import java.math.BigDecimal;

import com.mgdev.om_paye.validators.annotations.NotBlank;
import com.mgdev.om_paye.validators.annotations.ValidAccountNumber;
import com.mgdev.om_paye.validators.annotations.ValidAmount;
import com.mgdev.om_paye.validators.annotations.ValidMerchantCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementRequestDto {

    @NotBlank
    @ValidAccountNumber
    private String numeroCompteClient;

    @NotBlank
    @ValidMerchantCode
    private String codeMarchand;

    @ValidAmount
    private BigDecimal montant;

    private String description;
}