package com.mgdev.om_paye.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.TypeCompte;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comptes", indexes = {
    @Index(name = "idx_numero_compte", columnList = "numero_compte")
})
@Data  
@EqualsAndHashCode(callSuper= false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte extends AbstractEntity {
    
    @NotBlank(message = "Le numéro de compte est obligatoire")
    @Column(name = "numero_compte", nullable = false, unique = true, length = 50)
    private String numeroCompte;
    
    @NotBlank(message = "Le titulaire du compte est obligatoire")
    @Column(name = "titulaire_compte", nullable = false, length = 100)
    private String titulaireCompte;
    
    @DecimalMin(value = "0.0", message = "Le solde ne peut pas être négatif")
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal solde = BigDecimal.ZERO;
    
 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_compte", nullable = false, length = 20)
    @Builder.Default
    private TypeCompte typeCompte = TypeCompte.PERSONNEL;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CompteStatus status = CompteStatus.ACTIVE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    
    @OneToMany(mappedBy = "compteExpediteur", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> transactionsEmises = new ArrayList<>();
    
   
    @OneToMany(mappedBy = "compteDestinataire", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> transactionsRecues = new ArrayList<>();
    
    
    public void crediter(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        this.solde = this.solde.add(montant);
    }
    
    public void debiter(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (this.solde.compareTo(montant) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }
        this.solde = this.solde.subtract(montant);
    }

    
}