package net.karmak.conference.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.karmak.conference.domain.Conference;
import net.karmak.conference.domain.Talk;
import net.karmak.conference.dto.TalkDto;
import net.karmak.conference.mapper.TalkMapper;
import net.karmak.conference.repo.ConferenceRepository;
import net.karmak.conference.repo.TalkRepository;
import net.karmak.conference.validator.TalkValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalkService {
    private final TalkMapper talkMapper;
    private final TalkValidator validator;
    private final TalkRepository talkRepository;
    private final ConferenceRepository conferenceRepository;

    public TalkDto createTalk(Long conferenceId, TalkDto talkDto) {
        Conference conference = conferenceRepository.findWithTalksById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));
        validator.validate(conference, talkDto);

        Talk talk = talkMapper.dtoToEntity(talkDto);
        talk.setConference(conference);

        Talk savedTalk = talkRepository.save(talk);
        return talkMapper.entityToDto(savedTalk);
    }

    public List<TalkDto> getAllTalksForConference(Long conferenceId) {
        Conference conference = conferenceRepository.findWithTalksById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));
        return conference.getTalks().stream()
                .map(talkMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
