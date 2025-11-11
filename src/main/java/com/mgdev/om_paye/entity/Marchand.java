package com.mgdev.om_paye.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MARCHAND")
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(callSuper= true)

public class Marchand extends User {

    @Column(name = "code_marchand" , nullable = false, unique = true)
    private String codeMarchand;


    @Column( name = "business_name", nullable = false )
    private String businessName;

}
