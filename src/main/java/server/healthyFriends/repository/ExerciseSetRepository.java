package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.ExerciseSet;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Long> {
}
