package net.karmak.conference.service.mapper;

import net.karmak.conference.domain.TalkType;
import net.karmak.conference.domain.dto.TalkDto;
import net.karmak.conference.domain.entity.Talk;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TalkMapperTest {
    private TalkMapper talkMapper = new TalkMapperImpl();

    @Test
    public void testDtoToEntity() {
        TalkDto talkDto = new TalkDto();
        talkDto.setId(1L);
        talkDto.setTitle("Sample Talk");
        talkDto.setDescription("This is a sample talk");
        talkDto.setSpeakerName("John Doe");
        talkDto.setTalkType(TalkType.REPORT);

        Talk talk = talkMapper.dtoToEntity(talkDto);

        assertNotNull(talk);
        assertEquals(talkDto.getId(), talk.getId());
        assertEquals(talkDto.getTitle(), talk.getTitle());
        assertEquals(talkDto.getDescription(), talk.getDescription());
        assertEquals(talkDto.getSpeakerName(), talk.getSpeakerName());
        assertEquals(talkDto.getTalkType(), talk.getTalkType());
    }

    @Test
    public void testEntityToDto() {
        Talk talk = new Talk();
        talk.setId(1L);
        talk.setTitle("Sample Talk");
        talk.setDescription("This is a sample talk");
        talk.setSpeakerName("John Doe");
        talk.setTalkType(TalkType.REPORT);

        TalkDto talkDto = talkMapper.entityToDto(talk);

        assertNotNull(talkDto);
        assertEquals(talk.getId(), talkDto.getId());
        assertEquals(talk.getTitle(), talkDto.getTitle());
        assertEquals(talk.getDescription(), talkDto.getDescription());
        assertEquals(talk.getSpeakerName(), talkDto.getSpeakerName());
        assertEquals(talk.getTalkType(), talkDto.getTalkType());
    }
}
