package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.BodycompositionRecord;

import java.time.LocalDate;

@Repository
public interface BodyInfoRepository extends JpaRepositoryImplementation<BodycompositionRecord, Long> {

    boolean existsByuserIdAndDate(Long userId, LocalDate date);
}
