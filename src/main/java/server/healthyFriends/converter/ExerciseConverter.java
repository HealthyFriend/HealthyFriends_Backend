package server.healthyFriends.converter;

import org.springframework.data.domain.Page;
import server.healthyFriends.domain.entity.Exercise;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;
import server.healthyFriends.web.dto.response.ObjectiveResponse;

import java.util.List;
import java.util.stream.Collectors;

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

    public static ExerciseResponse.singleExercise singleExercise(Exercise exercise) {
        return ExerciseResponse.singleExercise.builder()
                .exerciseCategory(exercise.getExercise_category())
                .exerciseName(exercise.getExercise_name())
                .withWeight(exercise.getIsWeight())
                .build();
    }

    public static ExerciseResponse.getExerciseResponse getExerciseResponse(List<Exercise> exercises) {
        List<ExerciseResponse.singleExercise> singleExerciseList = exercises.stream()
                .map(ExerciseConverter::singleExercise)
                .toList();

        return ExerciseResponse.getExerciseResponse.builder()
                //.isLast(exercises.isLast())
                //.isFirst(exercises.isFirst())
                //.totalPage(exercises.getTotalPages())
                //.listSize(exercises.getSize())
                .exerciseList(singleExerciseList)
                .build();

    }
}
