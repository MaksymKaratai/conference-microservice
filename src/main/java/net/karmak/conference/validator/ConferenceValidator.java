package net.karmak.conference.validator;

import lombok.RequiredArgsConstructor;
import net.karmak.conference.dto.ConferenceDto;
import net.karmak.conference.exception.ValidationException;
import net.karmak.conference.repo.ConferenceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ConferenceValidator {
    private final ConferenceRepository conferenceRepository;

    @Transactional(readOnly = true)
    public void validate(ConferenceDto dto) {
        validateDates(dto);
        if (conferenceRepository.existsByName(dto.getName())) {
            throw new ValidationException(409, "Name [" + dto.getName() + "] already in use");
        }
    }

    public void validateForUpdate(ConferenceDto dto) {
        validateDates(dto);
    }

    private static void validateDates(ConferenceDto dto) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(400, "Start["+startDate+"] and end[" + endDate + "] dates overlaps");
        }
    }
}
