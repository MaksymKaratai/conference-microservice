package net.karmak.conference.service.validator;

import net.karmak.conference.domain.TalkType;
import net.karmak.conference.domain.dto.TalkDto;
import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.domain.entity.Talk;
import net.karmak.conference.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TalkValidatorTest {

    @Mock
    private Conference conference;

    @InjectMocks
    private TalkValidator talkValidator;

    @Test
    public void whenSpeakerAlreadyHas3TalksInConferenceThenValidateThrowsException() {
        String speakerName = "John Doe";
        Set<Talk> talks = new HashSet<>();
        talks.add(new Talk(1L, "Title 1", "Description 1", speakerName, TalkType.REPORT,conference));
        talks.add(new Talk(2L, "Title 2", "Description 2", speakerName, TalkType.MASTER_CLASS,conference));
        talks.add(new Talk(3L, "Title 3", "Description 3", speakerName, TalkType.WORKSHOP,conference));
        when(conference.getTalks()).thenReturn(talks);
        when(conference.getStartDate()).thenReturn(LocalDate.now().plusMonths(2));


        TalkDto talkDto = new TalkDto();
        talkDto.setSpeakerName(speakerName);

        assertThrows(ValidationException.class, () -> talkValidator.validate(conference, talkDto));
    }

    @Test
    public void whenConferenceStartDateIsNotMonthAheadThenValidateThrowsException() {
        
        LocalDate currentDate = LocalDate.now();
        LocalDate conferenceStartDate = currentDate.plusDays(15); // Less than a month ahead
        when(conference.getStartDate()).thenReturn(conferenceStartDate);

        TalkDto talkDto = new TalkDto();

        assertThrows(ValidationException.class, () -> talkValidator.validate(conference, talkDto));
    }

    @Test
    public void whenTalkTitleIsDuplicateThenValidateThrowsException() {
        
        String talkTitle = "Title";
        Set<Talk> talks = new HashSet<>();
        talks.add(new Talk(1L, talkTitle, "Description 1", "Speaker 1", TalkType.REPORT,conference));
        when(conference.getTalks()).thenReturn(talks);
        when(conference.getStartDate()).thenReturn(LocalDate.now().plusMonths(2));


        TalkDto talkDto = new TalkDto();
        talkDto.setTitle(talkTitle);

        assertThrows(ValidationException.class, () -> talkValidator.validate(conference, talkDto));
    }

    @Test
    public void whenTalkIsValidThenValidateDoesNotThrowException() {
        
        LocalDate currentDate = LocalDate.now();
        LocalDate conferenceStartDate = currentDate.plusMonths(1); // A month ahead
        when(conference.getStartDate()).thenReturn(conferenceStartDate);

        Set<Talk> talks = new HashSet<>();
        talks.add(new Talk(1L, "Title 1", "Description 1", "Speaker 1", TalkType.REPORT,conference));
        talks.add(new Talk(2L, "Title 2", "Description 2", "Speaker 2", TalkType.MASTER_CLASS,conference));
        talks.add(new Talk(3L, "Title 3", "Description 3", "Speaker 3", TalkType.WORKSHOP,conference));
        when(conference.getTalks()).thenReturn(talks);

        TalkDto talkDto = new TalkDto();
        talkDto.setTitle("New Talk");
        talkDto.setSpeakerName("New Speaker");

        assertDoesNotThrow(() -> talkValidator.validate(conference, talkDto));
    }
}
