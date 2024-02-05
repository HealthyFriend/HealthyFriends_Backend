package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.ExerciseRecord;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {
}
