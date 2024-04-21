package net.karmak.conference.service.impl;

import jakarta.persistence.EntityNotFoundException;
import net.karmak.conference.adapter.sql.ConferenceDao;
import net.karmak.conference.domain.dto.ConferenceDto;
import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.service.exception.ValidationException;
import net.karmak.conference.service.mapper.ConferenceMapper;
import net.karmak.conference.service.validator.ConferenceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasicConferenceServiceTest {
    @Mock
    private ConferenceValidator conferenceValidator;

    @Mock
    private ConferenceMapper conferenceMapper;

    @Mock
    private ConferenceDao conferenceDao;

    @InjectMocks
    private BasicConferenceService conferenceService;

    @Test
    void testCreateConference_Success() {
        // Given
        ConferenceDto inputDto = ConferenceDto.builder()
                .name("Conference Name")
                .topic("Conference Topic")
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 3))
                .numberOfParticipants(120)
                .build();

        Conference conference = Conference.builder()
                .name(inputDto.getName())
                .topic(inputDto.getTopic())
                .startDate(inputDto.getStartDate())
                .endDate(inputDto.getEndDate())
                .numberOfParticipants(inputDto.getNumberOfParticipants())
                .build();

        Conference savedConference = Conference.builder()
                .id(1L)
                .name(conference.getName())
                .topic(conference.getTopic())
                .startDate(conference.getStartDate())
                .endDate(conference.getEndDate())
                .numberOfParticipants(conference.getNumberOfParticipants())
                .build();

        when(conferenceMapper.dtoToEntity(inputDto)).thenReturn(conference);
        when(conferenceDao.save(conference)).thenReturn(savedConference);
        when(conferenceMapper.entityToDto(savedConference)).thenReturn(inputDto);

        // When
        ConferenceDto resultDto = conferenceService.createConference(inputDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(inputDto.getName(), resultDto.getName());
        assertEquals(inputDto.getTopic(), resultDto.getTopic());
        assertEquals(inputDto.getStartDate(), resultDto.getStartDate());
        assertEquals(inputDto.getEndDate(), resultDto.getEndDate());
        assertEquals(inputDto.getNumberOfParticipants(), resultDto.getNumberOfParticipants());

        verify(conferenceValidator, times(1)).validate(inputDto);
        verify(conferenceDao, times(1)).save(conference);
    }

    @Test
    void testCreateConference_WithValidationException() {
        // Given
        ConferenceDto inputDto = ConferenceDto.builder()
                .name("Conference Name")
                .topic("Conference Topic")
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 4, 30)) // Invalid end date
                .numberOfParticipants(120)
                .build();

        doThrow(new ValidationException(400, "Start[2024-05-01] and end[2024-04-30] dates overlap"))
                .when(conferenceValidator).validate(inputDto);

        // When & Then
        assertThrows(ValidationException.class, () -> conferenceService.createConference(inputDto));

        verify(conferenceValidator, times(1)).validate(inputDto);
        verify(conferenceDao, never()).save(any());
    }

    @Test
    void testUpdateConference_Success() {
        // Given
        Long conferenceId = 1L;
        ConferenceDto updateDto = ConferenceDto.builder()
                .name("Updated Conference Name")
                .topic("Updated Conference Topic")
                .startDate(LocalDate.of(2024, 5, 5))
                .endDate(LocalDate.of(2024, 5, 7))
                .numberOfParticipants(150)
                .build();

        Conference existingConference = Conference.builder()
                .id(conferenceId)
                .name("Existing Conference Name")
                .topic("Existing Conference Topic")
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 3))
                .numberOfParticipants(100)
                .build();

        Conference updatedConference = Conference.builder()
                .id(conferenceId)
                .name(updateDto.getName())
                .topic(updateDto.getTopic())
                .startDate(updateDto.getStartDate())
                .endDate(updateDto.getEndDate())
                .numberOfParticipants(updateDto.getNumberOfParticipants())
                .build();

        when(conferenceDao.findById(conferenceId)).thenReturn(Optional.of(existingConference));
        when(conferenceDao.save(existingConference)).thenReturn(updatedConference);
        when(conferenceMapper.entityToDto(updatedConference)).thenReturn(updateDto);

        // When
        ConferenceDto resultDto = conferenceService.updateConference(conferenceId, updateDto);

        // Then
        assertNotNull(resultDto);
        assertEquals(updateDto.getName(), resultDto.getName());
        assertEquals(updateDto.getTopic(), resultDto.getTopic());
        assertEquals(updateDto.getStartDate(), resultDto.getStartDate());
        assertEquals(updateDto.getEndDate(), resultDto.getEndDate());
        assertEquals(updateDto.getNumberOfParticipants(), resultDto.getNumberOfParticipants());

        verify(conferenceValidator, times(1)).validateForUpdate(updateDto);
        verify(conferenceDao, times(1)).findById(conferenceId);
        verify(conferenceDao, times(1)).save(existingConference);
    }

    @Test
    void testUpdateConference_ConferenceNotFound() {
        // Given
        Long conferenceId = 1L;
        ConferenceDto updateDto = ConferenceDto.builder()
                .name("Updated Conference Name")
                .topic("Updated Conference Topic")
                .startDate(LocalDate.of(2024, 5, 5))
                .endDate(LocalDate.of(2024, 5, 7))
                .numberOfParticipants(150)
                .build();

        when(conferenceDao.findById(conferenceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> conferenceService.updateConference(conferenceId, updateDto));

        verify(conferenceDao, times(1)).findById(conferenceId);
        verify(conferenceDao, never()).save(any());
    }
}
