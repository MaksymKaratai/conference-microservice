package net.karmak.conference.service;

import net.karmak.conference.domain.dto.TalkDto;

import java.util.List;

public interface TalkService {
    TalkDto createTalk(Long conferenceId, TalkDto talkDto);
    List<TalkDto> getAllTalksForConference(Long conferenceId);
}
