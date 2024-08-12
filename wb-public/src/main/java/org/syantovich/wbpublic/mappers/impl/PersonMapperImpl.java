package org.syantovich.wbpublic.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.syantovich.wbpublic.domain.AuthorityEntity;
import org.syantovich.wbpublic.domain.PersonEntity;
import org.syantovich.wbpublic.dto.PersonDto;
import org.syantovich.wbpublic.dto.PersonWithPasswordDto;
import org.syantovich.wbpublic.mappers.PersonMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonMapperImpl implements PersonMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public PersonDto toDto(PersonEntity personEntity) {
        if (personEntity == null) {
            return null;
        }

        return PersonDto.builder()
                .id(personEntity.getId())
                .name(personEntity.getName())
                .email(personEntity.getEmail())
                .isVerified(personEntity.getIsVerified())
                .authorities(personEntity.getPerms().stream()
                        .map(perm -> {
                            String[] parts = perm.getName().split("_");
                            return parts.length > 1 ? parts[1] : perm.getName();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public PersonEntity toEntity(PersonWithPasswordDto personDto) {
        if (personDto == null) {
            return null;
        }
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personDto.getId());
        personEntity.setName(personDto.getName());
        personEntity.setEmail(personDto.getEmail());
        personEntity.setPassword(passwordEncoder.encode(personDto.getPassword()));
        personEntity.setIsVerified(personDto.getIsVerified());
        personEntity.setPerms(personDto.getAuthorities().stream().map(role -> {
            var authorityEntity = new AuthorityEntity();
            authorityEntity.setName(role);
            authorityEntity.setPerson(personEntity);
            return authorityEntity;
        }).collect(Collectors.toList()));
        return personEntity;
    }

    @Override
    public List<PersonDto> toDtoList(List<PersonEntity> personEntities) {
        if (personEntities == null) {
            return null;
        }
        return personEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonEntity> toEntityList(List<PersonWithPasswordDto> personDtos) {
        if (personDtos == null) {
            return null;
        }
        return personDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PersonWithPasswordDto toDtoWithPassword(PersonEntity personEntity) {
        if (personEntity == null) {
            return null;
        }

        return PersonWithPasswordDto.builder()
                .id(personEntity.getId())
                .name(personEntity.getName())
                .email(personEntity.getEmail())
                .isVerified(personEntity.getIsVerified())
                .authorities(personEntity.getPerms().stream()
                        .map(perm -> {
                            String[] parts = perm.getName().split("_");
                            return parts.length > 1 ? parts[1] : perm.getName();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<PersonWithPasswordDto> toDtoWithPasswordList(List<PersonEntity> personEntities) {
        if (personEntities == null) {
            return null;
        }
        return personEntities.stream()
                .map(this::toDtoWithPassword)
                .collect(Collectors.toList());
    }
}
