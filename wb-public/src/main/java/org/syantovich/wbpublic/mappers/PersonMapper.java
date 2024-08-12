package org.syantovich.wbpublic.mappers;


import org.syantovich.wbpublic.domain.PersonEntity;
import org.syantovich.wbpublic.dto.PersonDto;
import org.syantovich.wbpublic.dto.PersonWithPasswordDto;

import java.util.List;

public interface PersonMapper {
    PersonWithPasswordDto toDtoWithPassword(PersonEntity personEntity);
    List<PersonWithPasswordDto> toDtoWithPasswordList(List<PersonEntity> personEntities);
    PersonDto toDto(PersonEntity personEntity);
    PersonEntity toEntity(PersonWithPasswordDto personDto);
    List<PersonDto> toDtoList(List<PersonEntity> personEntities);
    List<PersonEntity> toEntityList(List<PersonWithPasswordDto> personDtos);
}
