package com.mgdev.om_paye.entity;

import java.util.ArrayList;
import java.util.List;

import com.mgdev.om_paye.enums.UserRole;
import com.mgdev.om_paye.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_phone", columnList = "phone_number"),
    @Index(name = "idx_nci", columnList = "nci")
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)

@Data  
@EqualsAndHashCode(callSuper= false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity {
    
    private String name;
    
    private String email;
    
    private String nci;
    

    @Column(name = "phone_number")
    private String phoneNumber;
    
 
    private String password; 
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.CLIENT;
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Compte> comptes = new ArrayList<>();
    
    
    public void addCompte(Compte compte) {
        comptes.add(compte);
        compte.setUser(this);
    }
    
    public void removeCompte(Compte compte) {
        comptes.remove(compte);
        compte.setUser(null);
    }
}