package net.karmak.conference.service.mapper;

import net.karmak.conference.domain.dto.ConferenceDto;
import net.karmak.conference.domain.entity.Conference;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConferenceMapperTest {
    private final ConferenceMapper conferenceMapper = new ConferenceMapperImpl();

    @Test
    public void testDtoToEntity() {
        ConferenceDto dto = ConferenceDto.builder()
                .id(1L)
                .name("Test Conference")
                .topic("Technology")
                .startDate(LocalDate.of(2024, 4, 20))
                .endDate(LocalDate.of(2024, 4, 22))
                .numberOfParticipants(150)
                .build();

        Conference conference = conferenceMapper.dtoToEntity(dto);

        assertEquals(dto.getId(), conference.getId());
        assertEquals(dto.getName(), conference.getName());
        assertEquals(dto.getTopic(), conference.getTopic());
        assertEquals(dto.getStartDate(), conference.getStartDate());
        assertEquals(dto.getEndDate(), conference.getEndDate());
        assertEquals(dto.getNumberOfParticipants(), conference.getNumberOfParticipants());
    }

    @Test
    public void testEntityToDto() {
        Conference conference = Conference.builder()
                .id(1L)
                .name("Test Conference")
                .topic("Technology")
                .startDate(LocalDate.of(2024, 4, 20))
                .endDate(LocalDate.of(2024, 4, 22))
                .numberOfParticipants(150)
                .build();

        ConferenceDto dto = conferenceMapper.entityToDto(conference);

        assertEquals(conference.getId(), dto.getId());
        assertEquals(conference.getName(), dto.getName());
        assertEquals(conference.getTopic(), dto.getTopic());
        assertEquals(conference.getStartDate(), dto.getStartDate());
        assertEquals(conference.getEndDate(), dto.getEndDate());
        assertEquals(conference.getNumberOfParticipants(), dto.getNumberOfParticipants());
    }
}
