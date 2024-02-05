package server.healthyFriends.service.exercise;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.converter.ExerciseConverter;
import server.healthyFriends.domain.entity.*;
import server.healthyFriends.domain.entity.mapping.ExerciseMapping;
import server.healthyFriends.repository.*;
import server.healthyFriends.service.exercise.ExerciseService;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMappingRepository exerciseMappingRepository;
    private final DayRecordRepository dayRecordRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final ExerciseSetRepository exerciseSetRepository;


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

    public ExerciseResponse.getExerciseResponse getExerciseResponse(Long userId, Long exerciseCode) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        List<Exercise> exercises = exerciseRepository.findByUserIdAndExerciseCode(userId, exerciseCode);

        return ExerciseConverter.getExerciseResponse(exercises);
    }

    public ExerciseResponse.addExerciseRecordResponse addExerciseRecord(Long userId, ExerciseRequest.exerciseRecordRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        DayRecord dayRecord = ExerciseConverter.getDayRecord(user,request);

        dayRecord = dayRecordRepository.save(dayRecord);

        dayRecord.getExerciseRecordList().forEach(exerciseRecord -> {
            exerciseRecord = exerciseRecordRepository.save(exerciseRecord);
            exerciseSetRepository.saveAll(exerciseRecord.getExerciseSetList());
        });

        return ExerciseConverter.addExerciseRecordResponse(dayRecord);

       /*
        dayRecordRepository.save(dayRecord);
        exerciseRecordRepository.saveAll(exerciseRecords); // ExerciseRecord 리스트 저장
        exerciseSetRepository.saveAll(exerciseRecords.stream()
                .flatMap(exerciseRecord -> exerciseRecord.getExerciseSetList().stream()) // ExerciseSet 리스트 펼치기
                .collect(Collectors.toList())); // ExerciseSet 리스트 저장
        */

    }



}
