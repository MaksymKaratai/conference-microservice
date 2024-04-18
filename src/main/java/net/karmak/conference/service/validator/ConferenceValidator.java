package net.karmak.conference.service.validator;

import lombok.RequiredArgsConstructor;
import net.karmak.conference.domain.dto.ConferenceDto;
import net.karmak.conference.service.exception.ValidationException;
import net.karmak.conference.adapter.sql.ConferenceDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ConferenceValidator {
    private final ConferenceDao conferenceDao;

    @Transactional(readOnly = true)
    public void validate(ConferenceDto dto) {
        validateDates(dto);
        if (conferenceDao.existsByName(dto.getName())) {
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
