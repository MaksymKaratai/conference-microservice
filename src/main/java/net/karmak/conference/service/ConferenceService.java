package net.karmak.conference.service;

import net.karmak.conference.domain.dto.ConferenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceService {
    ConferenceDto createConference(ConferenceDto conferenceDto);
    Page<ConferenceDto> getAllConferences(Pageable page);
    ConferenceDto updateConference(Long conferenceId, ConferenceDto conferenceDto);
}
