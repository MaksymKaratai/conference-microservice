package net.karmak.conference.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conference_seq")
    @SequenceGenerator(name = "conference_seq", sequenceName = "conference_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    private String topic;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Positive
    private int numberOfParticipants;

    @EqualsAndHashCode.Exclude
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Talk> talks = new HashSet<>();
}

