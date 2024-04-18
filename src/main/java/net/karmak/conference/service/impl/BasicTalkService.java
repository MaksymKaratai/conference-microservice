package net.karmak.conference.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.karmak.conference.domain.entity.Conference;
import net.karmak.conference.domain.entity.Talk;
import net.karmak.conference.service.TalkService;
import net.karmak.conference.domain.dto.TalkDto;
import net.karmak.conference.service.mapper.TalkMapper;
import net.karmak.conference.adapter.sql.ConferenceDao;
import net.karmak.conference.adapter.sql.TalkDao;
import net.karmak.conference.service.validator.TalkValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicTalkService implements TalkService {
    private final TalkMapper talkMapper;
    private final TalkValidator validator;
    private final TalkDao talkDao;
    private final ConferenceDao conferenceDao;

    public TalkDto createTalk(Long conferenceId, TalkDto talkDto) {
        Conference conference = conferenceDao.findWithTalksById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));
        validator.validate(conference, talkDto);

        Talk talk = talkMapper.dtoToEntity(talkDto);
        talk.setConference(conference);

        Talk savedTalk = talkDao.save(talk);
        return talkMapper.entityToDto(savedTalk);
    }

    public List<TalkDto> getAllTalksForConference(Long conferenceId) {
        Conference conference = conferenceDao.findWithTalksById(conferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Conference not found with id: " + conferenceId));
        return conference.getTalks().stream()
                .map(talkMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
