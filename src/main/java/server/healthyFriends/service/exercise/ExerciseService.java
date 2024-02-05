package server.healthyFriends.service.exercise;

import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

public interface ExerciseService {
    ExerciseResponse.addExerciseResponse addExercise(Long userId, ExerciseRequest.addExerciseRequest request);
    ExerciseResponse.getExerciseResponse getExerciseResponse(Long userId, Long exerciseCode);
    ExerciseResponse.addExerciseDayRecordResponse addExerciseDayRecord(Long userId, ExerciseRequest.exerciseRecordRequest request);
    ExerciseResponse.getExerciseDayRecordResponse updateExerciseDayRecord(Long userId, ExerciseRequest.exerciseRecordRequest request);
}
