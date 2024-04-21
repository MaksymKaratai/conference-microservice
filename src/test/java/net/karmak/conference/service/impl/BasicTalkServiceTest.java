package net.karmak.conference.service.impl;

import jakarta.persistence.EntityNotFoundException;
import net.karmak.conference.adapter.sql.ConferenceDao;
import net.karmak.conference.adapter.sql.TalkDao;
import net.karmak.conference.domain.TalkType;
import net.karmak.conference.domain.dto.TalkDto;
import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.domain.entity.Talk;
import net.karmak.conference.service.mapper.TalkMapper;
import net.karmak.conference.service.validator.TalkValidator;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasicTalkServiceTest {
    @Mock
    private TalkDao talkDao;
    @Mock
    private ConferenceDao conferenceDao;
    @Mock
    private TalkMapper talkMapper;
    @Mock
    private TalkValidator talkValidator;
    @InjectMocks
    private BasicTalkService basicTalkService;

    @Test
    public void testCreateTalk_Success() {
        // Mock conference and talkDto
        Long conferenceId = 1L;
        Conference conference = Conference.builder()
                .id(conferenceId)
                .name("Test Conference")
                .startDate(LocalDate.now().plusMonths(2))
                .endDate(LocalDate.now().plusMonths(3))
                .build();

        TalkDto talkDto = TalkDto.builder()
                .title("Test Talk")
                .description("This is a test talk")
                .speakerName("John Doe")
                .talkType(TalkType.REPORT)
                .build();

        // Mock conferenceDao behavior
        when(conferenceDao.findWithTalksById(conferenceId)).thenReturn(Optional.of(conference));


        // Mock talkDao behavior
        Talk savedTalk = Talk.builder()
                .id(1L)
                .title(talkDto.getTitle())
                .description(talkDto.getDescription())
                .speakerName(talkDto.getSpeakerName())
                .talkType(talkDto.getTalkType())
                .conference(conference)
                .build();
        when(talkMapper.dtoToEntity(talkDto)).thenReturn(savedTalk);
        when(talkDao.save(any(Talk.class))).thenReturn(savedTalk);

        // Mock talkMapper behavior
        when(talkMapper.entityToDto(savedTalk)).thenReturn(talkDto);

        // Call the method under test
        TalkDto createdTalkDto = basicTalkService.createTalk(conferenceId, talkDto);

        // Verify interactions
        verify(conferenceDao).findWithTalksById(conferenceId);
        verify(talkValidator).validate(conference, talkDto);
        verify(talkDao).save(any(Talk.class));
        verify(talkMapper).entityToDto(savedTalk);

        assertNotNull(createdTalkDto);
        assertEquals(talkDto.getTitle(), createdTalkDto.getTitle());
        assertEquals(talkDto.getDescription(), createdTalkDto.getDescription());
        assertEquals(talkDto.getSpeakerName(), createdTalkDto.getSpeakerName());
        assertEquals(talkDto.getTalkType(), createdTalkDto.getTalkType());
    }

    @Test
    public void testCreateTalk_ConferenceNotFound() {
        Long conferenceId = 1L;
        when(conferenceDao.findWithTalksById(conferenceId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> basicTalkService.createTalk(conferenceId, new TalkDto()));
        assertEquals("Conference not found with id: " + conferenceId, exception.getMessage());

        verify(conferenceDao).findWithTalksById(conferenceId);
        verifyNoInteractions(talkValidator);
        verifyNoInteractions(talkDao);
        verifyNoInteractions(talkMapper);
    }
}
