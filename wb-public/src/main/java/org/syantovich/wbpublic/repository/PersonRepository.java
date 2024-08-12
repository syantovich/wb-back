package org.syantovich.wbpublic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.syantovich.wbpublic.domain.PersonEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
   Optional<PersonEntity> findByEmail(String email);
   Optional<PersonEntity> findById(UUID id);
   List<PersonEntity> findByIsVerifiedFalseAndCreatedAtBefore(Date date);
}
