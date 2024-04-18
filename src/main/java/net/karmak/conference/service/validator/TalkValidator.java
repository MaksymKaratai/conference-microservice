package net.karmak.conference.service.validator;

import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.domain.dto.TalkDto;
import net.karmak.conference.service.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TalkValidator {

    public void validate(Conference conference, TalkDto dto) {
        LocalDate currentDate = LocalDate.now();
        LocalDate conferenceStartDate = conference.getStartDate();
        if (currentDate.plusMonths(1).isAfter(conferenceStartDate)) {
            throw new ValidationException(400, "Talk submission is allowed only up to one month before the conference start date.");
        }
        if (countSpeakerTalks(conference, dto.getSpeakerName()) >= 3) {
            throw new ValidationException(400, "Speaker has reached the maximum limit of 3 talks for this conference.");
        }
        if (isTalkTitleDuplicate(conference, dto.getTitle())) {
            throw new ValidationException(409, "A talk with the same title already exists in this conference.");
        }
    }

    private int countSpeakerTalks(Conference conference, String speakerName) {
        return conference.getTalks().stream()
                .filter(talk -> talk.getSpeakerName().equalsIgnoreCase(speakerName))
                .mapToInt(talk -> 1)
                .sum();
    }

    private boolean isTalkTitleDuplicate(Conference conference, String talkTitle) {
        return conference.getTalks().stream()
                .anyMatch(talk -> talk.getTitle().equalsIgnoreCase(talkTitle));
    }
}
