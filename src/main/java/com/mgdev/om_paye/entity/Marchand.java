package com.mgdev.om_paye.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@DiscriminatorValue("MARCHAND")
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(callSuper= true)

public class Marchand extends User {

    @Column(name = "code_marchand" , nullable = true)
    private String codeMarchand;


    @Column( name = "business_name", nullable = true )
    private String businessName;
    
}
