package net.karmak.conference.service.mapper;

import net.karmak.conference.domain.entity.Talk;
import net.karmak.conference.domain.dto.TalkDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TalkMapper {
    Talk dtoToEntity(TalkDto TalkDto);
    TalkDto entityToDto(Talk Talk);
}