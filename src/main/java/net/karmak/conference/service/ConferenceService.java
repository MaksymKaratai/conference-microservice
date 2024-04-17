package net.karmak.conference.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.karmak.conference.domain.Conference;
import net.karmak.conference.dto.ConferenceDto;
import net.karmak.conference.mapper.ConferenceMapper;
import net.karmak.conference.repo.ConferenceRepository;
import net.karmak.conference.validator.ConferenceValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceValidator validator;
    private final ConferenceMapper conferenceMapper;
    private final ConferenceRepository conferenceRepository;

    @Transactional
    public ConferenceDto createConference(ConferenceDto conferenceDto) {
        validator.validate(conferenceDto);
        Conference conference = conferenceMapper.dtoToEntity(conferenceDto);
        Conference savedConference = conferenceRepository.save(conference);
        return conferenceMapper.entityToDto(savedConference);
    }

    @Transactional(readOnly = true)
    public Page<ConferenceDto> getAllConferences(Pageable page) {
        Page<Conference> conferences = conferenceRepository.findAll(page);
        return conferences.map(conferenceMapper::entityToDto);
    }

    @Transactional
    public ConferenceDto updateConference(Long conferenceId, ConferenceDto conferenceDto) {
        validator.validateForUpdate(conferenceDto);
        Conference existingConference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));

        // Update existingConference with data from conferenceDto
        existingConference.setName(conferenceDto.getName());
        existingConference.setTopic(conferenceDto.getTopic());
        existingConference.setStartDate(conferenceDto.getStartDate());
        existingConference.setEndDate(conferenceDto.getEndDate());
        existingConference.setNumberOfParticipants(conferenceDto.getNumberOfParticipants());

        Conference updatedConference = conferenceRepository.save(existingConference);
        return conferenceMapper.entityToDto(updatedConference);
    }
}
