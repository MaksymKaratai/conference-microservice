package net.karmak.conference.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.karmak.conference.domain.TalkType;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "talk_seq")
    @SequenceGenerator(name = "talk_seq", sequenceName = "talk_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String speakerName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TalkType talkType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;
}
