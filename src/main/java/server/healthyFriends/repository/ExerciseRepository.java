package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.healthyFriends.domain.entity.Exercise;
import server.healthyFriends.domain.entity.User;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {

    @Query("SELECT e " +
            "FROM Exercise e INNER JOIN e.exerciseMappingList em " +
            "WHERE em.user.id = :userId " +
            "AND e.exercise_code = :exerciseCode")
    List<Exercise> findByUserIdAndExerciseCode(@Param("userId") Long userId, @Param("exerciseCode") Long exerciseCode);
}
