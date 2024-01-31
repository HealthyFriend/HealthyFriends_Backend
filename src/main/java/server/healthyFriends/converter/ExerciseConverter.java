package server.healthyFriends.converter;

import server.healthyFriends.domain.entity.Exercise;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

public class ExerciseConverter {

    public static Exercise toExercise(ExerciseRequest.addExerciseRequest request) {
        return Exercise.builder()
                .exercise_category(request.getExerciseCategory())
                .exercise_name(request.getExerciseName())
                .build();
    }

    public static ExerciseResponse.addExerciseResponse addExerciseResponse(Exercise exercise) {
        return ExerciseResponse.addExerciseResponse.builder()
                .exerciseId(exercise.getId())
                .build();
    }
}
