package com.mgdev.om_paye.dto.request;

import com.mgdev.om_paye.validators.annotations.ValidSenegalPhone;
import com.mgdev.om_paye.validators.annotations.ValidCni;
import com.mgdev.om_paye.validators.annotations.ValidEmail;
import com.mgdev.om_paye.validators.annotations.NotBlank;
import com.mgdev.om_paye.validators.annotations.UniqueEmail;
import com.mgdev.om_paye.validators.annotations.UniquePhoneNumber;
import com.mgdev.om_paye.validators.annotations.ValidAdminCode;
import com.mgdev.om_paye.validators.annotations.ValidMerchantCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank
    private String name;

    @ValidEmail
    @UniqueEmail
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    @ValidSenegalPhone
    @UniquePhoneNumber
    private String phoneNumber;

    @ValidCni
    private String nci;

    private String adresse;

    @ValidAdminCode
    private String adminCode;

    private String businessName;

    @ValidMerchantCode
    private String codeMarchand;
}
