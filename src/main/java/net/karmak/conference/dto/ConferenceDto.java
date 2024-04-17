package net.karmak.conference.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ConferenceDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String topic;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @Min(101)
    @Positive
    private int numberOfParticipants;
}