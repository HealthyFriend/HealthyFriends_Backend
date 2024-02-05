package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.DayRecord;

public interface DayRecordRepository extends JpaRepository<DayRecord, Long> {
}
