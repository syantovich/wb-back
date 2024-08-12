package org.syantovich.wbpublic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.syantovich.wbpublic.domain.AccessToken;

import java.util.UUID;

public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    AccessToken findByToken(String token);
    void deleteByToken(String token);
}
