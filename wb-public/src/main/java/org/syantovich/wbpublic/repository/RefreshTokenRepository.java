package org.syantovich.wbpublic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.syantovich.wbpublic.domain.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
