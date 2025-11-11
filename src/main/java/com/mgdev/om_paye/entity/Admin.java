package com.mgdev.om_paye.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@DiscriminatorValue("ADMIN")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(callSuper=true)

public class Admin extends User {

    @Column(name = "admin_code")
    private String adminCode;
    
}
