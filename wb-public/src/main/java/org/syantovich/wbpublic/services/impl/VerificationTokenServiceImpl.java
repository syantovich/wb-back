package org.syantovich.wbpublic.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.domain.PersonEntity;
import org.syantovich.wbpublic.domain.VerificationCodeEntity;
import org.syantovich.wbpublic.errors.CodeSendingException;
import org.syantovich.wbpublic.errors.InvalidCodeException;
import org.syantovich.wbpublic.errors.TooManyRequestsException;
import org.syantovich.wbpublic.repository.PersonRepository;
import org.syantovich.wbpublic.repository.VerificationTokenRepository;
import org.syantovich.wbpublic.services.MessageSender;
import org.syantovich.wbpublic.services.VerificationCodeService;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationCodeService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final PersonRepository personRepository;
    private final MessageSender messageSender;

    @Value("${code.expiration}")
    private long codeExpirationTime;

    //Время для отпрвки кода повторно
    @Value("${code.resend-minutes}")
    private int resendMinutes;

    @Value("${code.length}")
    private int codeLength;

    public String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10)); // Генерируем случайное число от 0 до 9
        }
        return code.toString();
    }

    @Transactional
    public boolean sendCode(PersonEntity person) {
        if (person.getIsVerified()) {
            throw new IllegalArgumentException("User is already verified");
        }

        Optional<VerificationCodeEntity> lastCode = verificationTokenRepository.findLatestByPersonId(person.getId());

        //логика по проверке времени отправки кода
        if (lastCode.isPresent()) {
            long diffInMillies = Math.abs(new Date().getTime() - lastCode.get().getCreatedAt().getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff < resendMinutes) {
                throw new TooManyRequestsException("You can only request a new code every " + TimeUnit.MILLISECONDS.toMinutes(resendMinutes) + " minutes");
            }
        }

        String code = generateCode();
        Date expiryDate = new Date(System.currentTimeMillis() + codeExpirationTime);

        VerificationCodeEntity verificationTokenEntity = new VerificationCodeEntity();
        verificationTokenEntity.setId(UUID.randomUUID());
        verificationTokenEntity.setCode(code);
        verificationTokenEntity.setExpiryDate(expiryDate);
        verificationTokenEntity.setPerson(person);
        verificationTokenRepository.save(verificationTokenEntity);

        boolean isSent = messageSender.send(person.getEmail(), "Your " + codeLength + "-digit code", "Your code is: " + code);

        if (!isSent) {
            throw new CodeSendingException("Failed to send verification code to " + person.getEmail());
        }
        return true;
    }

    @Transactional
    public boolean sendCode(UUID uuid, String email) {
        var person = personRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User with this email not found"));
        if (!person.getId().equals(uuid)) {
            throw new IllegalArgumentException("User with this email not found");
        }
        return sendCode(person);
    }

    @Transactional
    public boolean validateCode(String email, String code) {
        try {
            Optional<VerificationCodeEntity> codeEntity = verificationTokenRepository.findByCode(code);

            if (codeEntity.isEmpty()) {
                throw new RuntimeException();
            }
            PersonEntity person = codeEntity.get().getPerson();

            if (!person.getEmail().equals(email)) {
                throw new RuntimeException();
            }

            if (codeEntity.get().getExpiryDate().before(new Date())) {
                throw new RuntimeException();
            }

            person.setIsVerified(true);
            verificationTokenRepository.deleteAllByPersonId(person.getId());
        } catch (Exception e) {
            throw new InvalidCodeException("Invalid verification code.");
        }

        return false;
    }

    public void deleteCode(String code) {
        verificationTokenRepository.deleteByCode(code);
    }
}
