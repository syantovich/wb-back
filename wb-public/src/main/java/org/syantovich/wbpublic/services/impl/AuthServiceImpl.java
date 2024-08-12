package org.syantovich.wbpublic.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.domain.AccessToken;
import org.syantovich.wbpublic.domain.RefreshToken;
import org.syantovich.wbpublic.dto.*;
import org.syantovich.wbpublic.mappers.PersonMapper;
import org.syantovich.wbpublic.repository.PersonRepository;
import org.syantovich.wbpublic.services.AuthService;
import org.syantovich.wbpublic.services.TokenService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final PersonRepository personRepository;
    final PersonMapper personMapper;
    final TokenService tokenService;
    final PasswordEncoder passwordEncoder;

    @Value("${roles.default}")
    String[] defaultRoles;

    public AuthResponseDto registerPerson(RegisterDto registerDto) {
        if (personRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        PersonWithPasswordDto personDto = new PersonWithPasswordDto();
        personDto.setEmail(registerDto.getEmail());
        personDto.setPassword(registerDto.getPassword());
        personDto.setAuthorities(Arrays.stream(defaultRoles).toList());
        personDto.setName(registerDto.getName());
        personDto.setIsVerified(false);
        var personEntity = personMapper.toEntity(personDto);
        personRepository.save(personEntity);
        PersonDto savedPerson = personMapper.toDto(personRepository.findByEmail(registerDto.getEmail()).get());
        RefreshToken refreshToken = tokenService.createRefreshToken(personEntity);
        AccessToken token = tokenService.createToken(personEntity, refreshToken);
        return new AuthResponseDto(token.getToken(), refreshToken.getToken(), savedPerson);
    }

    public AuthResponseDto login(LoginDto loginDto) {

        var personEntity = personRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with this email not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), personEntity.getPassword())) { // Сравнение паролей
            throw new IllegalArgumentException("Password is incorrect");
        }
        var personDto = personMapper.toDto(personEntity);
        RefreshToken refreshToken = tokenService.createRefreshToken(personEntity);
        AccessToken token = tokenService.createToken(personEntity, refreshToken);
        return new AuthResponseDto(token.getToken(), refreshToken.getToken(), personDto);
    }
}
