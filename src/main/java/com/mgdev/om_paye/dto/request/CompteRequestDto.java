package com.mgdev.om_paye.dto.request;

import com.mgdev.om_paye.validators.annotations.NotBlank;
import com.mgdev.om_paye.validators.annotations.ValidCni;
import com.mgdev.om_paye.validators.annotations.ValidEmail;
import com.mgdev.om_paye.validators.annotations.ValidSenegalPhone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class CompteRequestDto {

    @NotBlank
    private String name;

    @ValidEmail
    private String email;

    @NotBlank
    @ValidCni
    private String nci;

    @ValidSenegalPhone
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotBlank
    private String typeCompte;

}