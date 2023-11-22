package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.Objective;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    Optional<Objective> findById(Long objectiveId);

    Optional<List<Objective>> findByUserId(Long userId);
}
