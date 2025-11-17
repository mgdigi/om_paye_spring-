package com.mgdev.om_paye.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.Transaction;
import com.mgdev.om_paye.enums.TransactionStatus;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByReferenceTransaction(String referenceTransaction);
    List<Transaction> findByCompteExpediteur(Compte compte);
    List<Transaction> findByCompteDestinataire(Compte compte);
    List<Transaction> findByStatus(TransactionStatus status);
    
    List<Transaction> findByCompteExpediteurOrCompteDestinataire(Compte expediteur, Compte destinataire);
    
    // Calculer le solde dynamiquement
    @Query("SELECT COALESCE(SUM(t.montantTransaction), 0) FROM Transaction t " +
           "WHERE t.compteDestinataire.id = :compteId AND t.status = 'COMPLETED'")
    BigDecimal sumCredits(@Param("compteId") UUID compteId);
    
    @Query("SELECT COALESCE(SUM(t.montantTransaction), 0) FROM Transaction t " +
           "WHERE t.compteExpediteur.id = :compteId AND t.status = 'COMPLETED'")
    BigDecimal sumDebits(@Param("compteId") UUID compteId);

    @Query("SELECT t FROM Transaction t WHERE (t.compteExpediteur = :compte OR t.compteDestinataire = :compte) AND (:type IS NULL OR t.typeTransaction = :type) AND t.status = 'COMPLETED' ORDER BY t.creationDate DESC")
    List<Transaction> findByCompteAndType(@Param("compte") Compte compte, @Param("type") com.mgdev.om_paye.enums.TypeTransaction type);
}