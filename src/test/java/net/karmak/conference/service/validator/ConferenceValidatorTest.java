package net.karmak.conference.service.validator;

import net.karmak.conference.adapter.sql.ConferenceDao;
import net.karmak.conference.domain.dto.ConferenceDto;
import net.karmak.conference.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConferenceValidatorTest {
    @Mock
    private ConferenceDao conferenceDao;

    @InjectMocks
    private ConferenceValidator conferenceValidator;

    @Test
    public void whenDtoHasValidDatesThenValidateDatesDoesNotThrowException() {
        String name = "new conf";
        ConferenceDto dto = ConferenceDto.builder()
                .name(name)
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5))
                .build();
        when(conferenceDao.existsByName(name)).thenReturn(false);
        // Validation should pass without throwing an exception
        conferenceValidator.validate(dto);
    }

    @Test
    public void whenDtoHasInvalidDatesThenValidateDatesThrowsException() {
        // Prepare invalid dates (endDate before startDate)
        ConferenceDto dto = ConferenceDto.builder()
                .startDate(LocalDate.of(2024, 5, 5))
                .endDate(LocalDate.of(2024, 5, 1))
                .build();

        // Validation should throw a ValidationException
        assertThrows(ValidationException.class, () -> conferenceValidator.validate(dto));
    }

    @Test
    public void whenDtoHasValidDatesThenValidateForUpdateDoesNotThrowException() {
        // Prepare valid dates
        ConferenceDto dto = ConferenceDto.builder()
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5))
                .build();

        // Validation should pass without throwing an exception
        conferenceValidator.validateForUpdate(dto);
    }

    @Test
    public void whenDtoHasInvalidDatesThenValidateForUpdateThrowsException() {
        // Prepare invalid dates (endDate before startDate)
        ConferenceDto dto = ConferenceDto.builder()
                .startDate(LocalDate.of(2024, 5, 5))
                .endDate(LocalDate.of(2024, 5, 1))
                .build();

        // Validation should throw a ValidationException
        assertThrows(ValidationException.class, () -> conferenceValidator.validateForUpdate(dto));
    }

    @Test
    public void whenDtoHasNonUniqueNameThenValidateThrowsException() {
        // Mock behavior of conferenceDao.existsByName
        String existingName = "Existing Conference";
        when(conferenceDao.existsByName(existingName)).thenReturn(true);

        // Prepare a ConferenceDto with a non-unique name
        ConferenceDto dto = ConferenceDto.builder()
                .name(existingName)
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5))
                .build();

        // Validation should throw a ValidationException for non-unique name
        assertThrows(ValidationException.class, () -> conferenceValidator.validate(dto));
    }
}
