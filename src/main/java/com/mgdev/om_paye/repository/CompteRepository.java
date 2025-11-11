package com.mgdev.om_paye.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;

@Repository
public interface CompteRepository extends JpaRepository<Compte, UUID> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
    Optional<Compte> findByCodeMarchand(String codeMarchand);
    List<Compte> findByUser(User user);
    List<Compte> findByUserId(UUID userId);
    boolean existsByNumeroCompte(String numeroCompte);
    boolean existsByCodeMarchand(String codeMarchand);
    boolean existsByUser_Email(String email);
    boolean existsByUser_PhoneNumber(String phoneNumber);
}