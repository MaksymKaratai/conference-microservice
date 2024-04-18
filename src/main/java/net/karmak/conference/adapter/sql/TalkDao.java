package net.karmak.conference.adapter.sql;

import net.karmak.conference.domain.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkDao extends JpaRepository<Talk, Long> {}
