package org.syantovich.wbpublic.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.syantovich.wbpublic.domain.PersonEntity;
import org.syantovich.wbpublic.repository.PersonRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PersonSchedulers {
    private final PersonRepository personRepository;

    @Value("${scheduler.persons.deleteUnverifiedUsers}")
    private boolean deleteUnverifiedUsers;

    @Value("${scheduler.persons.daysBeforeDeletion}")
    private int daysBeforeDeletion;

    private static final String CRON_EXPRESSION = "0 0 0 * * ?"; // Runs every day at midnight

    @Scheduled(cron = CRON_EXPRESSION)
    public void deleteUnverifiedUsers() {
        if (!deleteUnverifiedUsers) {
            return;
        }
        Date thresholdDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysBeforeDeletion));
        List<PersonEntity> unverifiedUsers = personRepository.findByIsVerifiedFalseAndCreatedAtBefore(thresholdDate);
        personRepository.deleteAll(unverifiedUsers);
    }
}
