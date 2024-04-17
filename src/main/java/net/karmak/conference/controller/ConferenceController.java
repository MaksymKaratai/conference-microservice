package net.karmak.conference.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.karmak.conference.dto.ConferenceDto;
import net.karmak.conference.dto.TalkDto;
import net.karmak.conference.service.ConferenceService;
import net.karmak.conference.service.TalkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/conferences")
public class ConferenceController {
    private final TalkService talkService;
    private final ConferenceService conferenceService;

    @GetMapping
    public Page<ConferenceDto> getAllConferences(Pageable pageable) {
        return conferenceService.getAllConferences(pageable);
    }

    @PostMapping
    public ResponseEntity<ConferenceDto> addConference(@Valid @RequestBody ConferenceDto dto) {
        ConferenceDto savedConferenceDto = conferenceService.createConference(dto);
        return new ResponseEntity<>(savedConferenceDto, HttpStatus.CREATED);
    }

    @PutMapping("/{conferenceId}")
    public ResponseEntity<ConferenceDto> updateConference(@PathVariable Long conferenceId,
                                                          @Valid @RequestBody ConferenceDto dto) {
        ConferenceDto updatedConferenceDto = conferenceService.updateConference(conferenceId, dto);
        return new ResponseEntity<>(updatedConferenceDto, HttpStatus.OK);
    }

    @PostMapping("/{conferenceId}/talks")
    public ResponseEntity<TalkDto> addTalkToConference(@PathVariable Long conferenceId,
                                                       @Valid @RequestBody TalkDto dto) {
        TalkDto savedTalkDto = talkService.createTalk(conferenceId, dto);
        return new ResponseEntity<>(savedTalkDto, HttpStatus.CREATED);
    }

    @GetMapping("/{conferenceId}/talks")
    public ResponseEntity<List<TalkDto>> getAllTalksForConference(@PathVariable Long conferenceId) {
        List<TalkDto> talkDtos = talkService.getAllTalksForConference(conferenceId);
        return new ResponseEntity<>(talkDtos, HttpStatus.OK);
    }
}
