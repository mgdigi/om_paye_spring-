package com.mgdev.om_paye.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgdev.om_paye.entity.OtpCode;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, java.util.UUID> {

    Optional<OtpCode> findByPhoneNumberAndCodeAndUsedFalse(String phoneNumber, String code);

    Optional<OtpCode> findFirstByPhoneNumberAndUsedFalseOrderByExpiresAtDesc(String phoneNumber);

    @Modifying
    @Query("UPDATE OtpCode o SET o.used = true WHERE o.phoneNumber = :phoneNumber AND o.used = false")
    void markAllAsUsedByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query("DELETE FROM OtpCode o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);

    boolean existsByPhoneNumberAndUsedFalse(String phoneNumber);
}