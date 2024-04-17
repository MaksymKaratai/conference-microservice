package net.karmak.conference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.karmak.conference.domain.TalkType;

@Data
public class TalkDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String speakerName;
    @NotNull
    private TalkType talkType;
}