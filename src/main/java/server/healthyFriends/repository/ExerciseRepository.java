package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {

}
