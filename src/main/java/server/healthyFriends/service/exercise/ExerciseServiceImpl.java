package server.healthyFriends.service.exercise;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.converter.ExerciseConverter;
import server.healthyFriends.domain.entity.Exercise;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.ExerciseMapping;
import server.healthyFriends.repository.ExerciseMappingRepository;
import server.healthyFriends.repository.ExerciseRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.exercise.ExerciseService;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMappingRepository exerciseMappingRepository;
    public ExerciseResponse.addExerciseResponse addExercise(Long userId, ExerciseRequest.addExerciseRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));


        Exercise exercise = ExerciseConverter.toExercise(request);

        if(exercise.getExercise_category().equals("하체")) {
            exercise.setExercise_code(1L);
        }
        else if(exercise.getExercise_category().equals("가슴")) {
            exercise.setExercise_code(2L);
        }
        else if(exercise.getExercise_category().equals("등")) {
            exercise.setExercise_code(3L);
        }
        else if (exercise.getExercise_category().equals("어깨")) {
            exercise.setExercise_code(4L);
        }
        else if (exercise.getExercise_category().equals("팔")) {
            exercise.setExercise_code(5L);
        }
        else if(exercise.getExercise_category().equals("복근")) {
            exercise.setExercise_code(6L);
        }
        exercise = exerciseRepository.save(exercise);

        ExerciseMapping exerciseMapping = new ExerciseMapping();
        exerciseMapping.setUser(user);
        exerciseMapping.setExercise(exercise);
        exerciseMappingRepository.save(exerciseMapping);

        return ExerciseConverter.addExerciseResponse(exercise);
    }
}
