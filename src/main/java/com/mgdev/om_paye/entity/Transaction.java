package com.mgdev.om_paye.entity;

import java.math.BigDecimal;

import com.mgdev.om_paye.enums.TransactionStatus;
import com.mgdev.om_paye.enums.TypeTransaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_reference", columnList = "reference_transaction"),
    @Index(name = "idx_expediteur", columnList = "compte_expediteur_id"),
    @Index(name = "idx_destinataire", columnList = "compte_destinataire_id"),
    @Index(name = "idx_type", columnList = "type_transaction")
})
@Data 
@EqualsAndHashCode(callSuper=  false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends AbstractEntity {
    
     @NotBlank(message = "La référence de transaction est obligatoire")
    @Column(name = "reference_transaction", nullable = false, unique = true, length = 100)
    private String referenceTransaction;
    
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    @Column(name = "montant_transaction", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantTransaction;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction", nullable = false, length = 30)
    private TypeTransaction typeTransaction;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    
 
    @Column(length = 500)
    private String description;
    
   
    @Column(name = "frais", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal frais = BigDecimal.ZERO;
    
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_expediteur_id", nullable = false)
    private Compte compteExpediteur;
    
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_destinataire_id")
    private Compte compteDestinataire;
    

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;
}
