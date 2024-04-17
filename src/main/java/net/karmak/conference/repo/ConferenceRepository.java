package net.karmak.conference.repo;

import net.karmak.conference.domain.Conference;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    boolean existsByName(String name);

    @EntityGraph(attributePaths = "talks")
    Optional<Conference> findWithTalksById(Long id);
}
