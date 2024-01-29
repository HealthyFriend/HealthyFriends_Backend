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

    @Query("SELECT MIN(r.date) FROM BodycompositionRecord r WHERE r.user.id = :userId AND r.bmi IS NOT NULL")
    LocalDate findEarliestBmiRecordDate(@Param("userId") Long userId);

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

    @Query("SELECT r.bmi, r.date FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "ORDER BY r.date ASC")
    List<Object[]> findDailyBmiChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT YEAR(r.date) AS year, MONTH(r.date) AS month, AVG(r.weight) AS averageWeight " +
            "FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "GROUP BY YEAR(r.date), MONTH(r.date)")
    List<Object[]> findMonthlyWeightChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT YEAR(r.date) AS year, MONTH(r.date) AS month, AVG(r.skeletal_muscle_mass) AS averageMuscle " +
            "FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "GROUP BY YEAR(r.date), MONTH(r.date)")
    List<Object[]> findMonthlyMuscleChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT YEAR(r.date) AS year, MONTH(r.date) AS month, AVG(r.body_fat_mass) AS averageFat " +
            "FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "GROUP BY YEAR(r.date), MONTH(r.date)")
    List<Object[]> findMonthlyFatChange(@Param("userId")Long userId, @Param("date")LocalDate date);

    @Query("SELECT YEAR(r.date) AS year, MONTH(r.date) AS month, AVG(r.bmi) AS averageBmi " +
            "FROM BodycompositionRecord r " +
            "WHERE r.user.id = :userId " +
            "AND r.date >= :date " +
            "GROUP BY YEAR(r.date), MONTH(r.date)")
    List<Object[]> findMonthlyBmiChange(@Param("userId")Long userId, @Param("date")LocalDate date);

}

