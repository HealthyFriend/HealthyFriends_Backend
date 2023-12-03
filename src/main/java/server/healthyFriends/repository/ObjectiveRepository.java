package server.healthyFriends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    Optional<Objective> findById(Long objectiveId);

    Page<Objective> findObjectviesByUser(User user, PageRequest pageRequest);
    Optional<List<Objective>> findByUserId(Long userId);
}
