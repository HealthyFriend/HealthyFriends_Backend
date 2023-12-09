package server.healthyFriends.repository;

import jakarta.persistence.Tuple;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BodyInfoRepository extends JpaRepositoryImplementation<BodycompositionRecord, Long> {

    boolean existsByuserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT MIN(r.date) FROM BodycompositionRecord r WHERE r.user.id = :userId")
    LocalDate findEarliestRecordDate(@Param("userId")Long userId);

    @Query("SELECT MIN(r.date) FROM BodycompositionRecord r WHERE r.user.id = :userId AND r.weight IS NOT NULL")
    LocalDate findEarliestWeightRecordDate(@Param("userId") Long userId);

    @Query("SELECT MIN(r.date) FROM BodycompositionRecord r WHERE r.user.id = :userId AND r.skeletal_muscle_mass IS NOT NULL")
    LocalDate findEarliestMuscleRecordDate(@Param("userId") Long userId);

    @Query("SELECT MIN(r.date) FROM BodycompositionRecord r WHERE r.user.id = :userId AND r.body_fat_mass IS NOT NULL")
    LocalDate findEarliestFatRecordDate(@Param("userId") Long userId);

    @Query("SELECT r.weight, r.date FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "ORDER BY r.date ASC")
    List<Object[]> findDailyWeightChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT r.skeletal_muscle_mass, r.date FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "ORDER BY r.date ASC")
    List<Object[]> findDailyMuscleChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT r.body_fat_mass, r.date FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "ORDER BY r.date ASC")
    List<Object[]> findDailyFatChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    /*
    @Query("SELECT r.weight FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "ORDER BY r.date ASC")
    List<BigDecimal> findDailyWeightChange2(@Param("userId")Long userId, @Param("date")LocalDate date);
    */
}

