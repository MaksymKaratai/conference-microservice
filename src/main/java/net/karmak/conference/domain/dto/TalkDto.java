package net.karmak.conference.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.karmak.conference.domain.TalkType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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