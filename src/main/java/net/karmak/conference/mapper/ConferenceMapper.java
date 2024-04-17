package net.karmak.conference.mapper;

import net.karmak.conference.domain.Conference;
import net.karmak.conference.dto.ConferenceDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConferenceMapper {
    Conference dtoToEntity(ConferenceDto conferenceDto);
    ConferenceDto entityToDto(Conference conference);
}

