package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.mapping.ExerciseMapping;

public interface ExerciseMappingRepository extends JpaRepository<ExerciseMapping,Long> {
}
