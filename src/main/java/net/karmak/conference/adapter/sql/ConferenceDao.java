package net.karmak.conference.adapter.sql;

import net.karmak.conference.domain.entity.Conference;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceDao extends JpaRepository<Conference, Long> {
    boolean existsByName(String name);

    @EntityGraph(attributePaths = "talks")
    Optional<Conference> findWithTalksById(Long id);
}
