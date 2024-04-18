package net.karmak.conference.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.service.ConferenceService;
import net.karmak.conference.domain.dto.ConferenceDto;
import net.karmak.conference.service.mapper.ConferenceMapper;
import net.karmak.conference.adapter.sql.ConferenceDao;
import net.karmak.conference.service.validator.ConferenceValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicConferenceService implements ConferenceService {
    private final ConferenceValidator validator;
    private final ConferenceMapper conferenceMapper;
    private final ConferenceDao conferenceDao;

    @Transactional
    public ConferenceDto createConference(ConferenceDto conferenceDto) {
        validator.validate(conferenceDto);
        Conference conference = conferenceMapper.dtoToEntity(conferenceDto);
        Conference savedConference = conferenceDao.save(conference);
        return conferenceMapper.entityToDto(savedConference);
    }

    @Transactional(readOnly = true)
    public Page<ConferenceDto> getAllConferences(Pageable page) {
        Page<Conference> conferences = conferenceDao.findAll(page);
        return conferences.map(conferenceMapper::entityToDto);
    }

    @Transactional
    public ConferenceDto updateConference(Long conferenceId, ConferenceDto conferenceDto) {
        validator.validateForUpdate(conferenceDto);
        Conference existingConference = conferenceDao.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));

        // Update existingConference with data from conferenceDto
        existingConference.setName(conferenceDto.getName());
        existingConference.setTopic(conferenceDto.getTopic());
        existingConference.setStartDate(conferenceDto.getStartDate());
        existingConference.setEndDate(conferenceDto.getEndDate());
        existingConference.setNumberOfParticipants(conferenceDto.getNumberOfParticipants());

        Conference updatedConference = conferenceDao.save(existingConference);
        return conferenceMapper.entityToDto(updatedConference);
    }
}
