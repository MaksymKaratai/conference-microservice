package net.karmak.conference.mapper;

import net.karmak.conference.domain.Talk;
import net.karmak.conference.dto.TalkDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TalkMapper {
    Talk dtoToEntity(TalkDto TalkDto);
    TalkDto entityToDto(Talk Talk);
}