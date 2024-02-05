package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.entity.DayRecord;

import java.time.LocalDate;
import java.util.List;

public interface DayRecordRepository extends JpaRepository<DayRecord, Long> {
    List<DayRecord> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
