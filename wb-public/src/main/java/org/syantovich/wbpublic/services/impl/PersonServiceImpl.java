package org.syantovich.wbpublic.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.dto.*;
import org.syantovich.wbpublic.mappers.PersonMapper;
import org.syantovich.wbpublic.repository.PersonRepository;
import org.syantovich.wbpublic.services.PersonService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonServiceImpl implements PersonService {
    final PersonRepository personRepository;
    final PersonMapper personMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var personEntity = personRepository.findByEmail(username);
        if (personEntity.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return personEntity.get();
    }

    public List<PersonDto> getAllUsers() {
        return personRepository.findAll().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto getPersonByEmail(String email) {
        var personEntity = personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with this email not found"));
        return personMapper.toDto(personEntity);
    }
}
