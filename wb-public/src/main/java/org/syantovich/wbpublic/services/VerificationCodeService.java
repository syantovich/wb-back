package org.syantovich.wbpublic.services;

import org.syantovich.wbpublic.domain.PersonEntity;

import java.util.UUID;

public interface VerificationCodeService {
    String generateCode();

    boolean validateCode(String email, String token);

    void deleteCode(String token);

    boolean sendCode(PersonEntity person);

    boolean sendCode(UUID id, String email);
}
