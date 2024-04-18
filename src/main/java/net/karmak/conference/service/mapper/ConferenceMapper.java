package net.karmak.conference.service.mapper;

import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.domain.dto.ConferenceDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConferenceMapper {
    Conference dtoToEntity(ConferenceDto conferenceDto);
    ConferenceDto entityToDto(Conference conference);
}

