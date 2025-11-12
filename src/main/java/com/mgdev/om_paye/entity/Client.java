package com.mgdev.om_paye.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@DiscriminatorValue("CLIENT")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(callSuper=true)

public class Client  extends User{
    private String adresse;
    
}
