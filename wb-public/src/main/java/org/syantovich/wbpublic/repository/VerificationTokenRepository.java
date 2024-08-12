package org.syantovich.wbpublic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.syantovich.wbpublic.domain.VerificationCodeEntity;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationCodeEntity, UUID> {
    Optional<VerificationCodeEntity> findByCode(String token);
    @Query("SELECT v FROM VerificationCodeEntity v WHERE v.person.id = :id ORDER BY v.createdAt DESC LIMIT 1")
    Optional<VerificationCodeEntity> findLatestByPersonId(@Param("id") UUID id);
    void deleteByCode(String token);
    void deleteAllByPersonId(UUID id);
}
