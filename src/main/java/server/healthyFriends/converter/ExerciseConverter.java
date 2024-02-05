package server.healthyFriends.converter;

import org.springframework.data.domain.Page;
import server.healthyFriends.domain.entity.*;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;
import server.healthyFriends.web.dto.response.ObjectiveResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public static ExerciseResponse.addExerciseRecordResponse addExerciseRecordResponse(DayRecord dayRecord) {
        return ExerciseResponse.addExerciseRecordResponse.builder()
                .dayExerciseRecordId(dayRecord.getId())
                .build();
    }
    public static DayRecord enrollDayRecord(User user, ExerciseRequest.exerciseRecordRequest request) {
        DayRecord dayRecord = new DayRecord();
        dayRecord.setDate(LocalDate.now());
        dayRecord.setTotal_time(request.getDayExerciseTime());

        List<ExerciseRecord> exerciseRecords = request.getDayExerciseRecord().stream()
                .map(singleExerciseRecord -> {
                    ExerciseRecord exerciseRecord = new ExerciseRecord();
                    exerciseRecord.setDate(LocalDate.now());
                    exerciseRecord.setExercise_category(singleExerciseRecord.getExerciseCategory());
                    exerciseRecord.setExercise_name(singleExerciseRecord.getExerciseName());
                    exerciseRecord.setMax_weight_rep(getMaxSetWeightRep(singleExerciseRecord.getSetInfos()));//exerciseSetRecord의 가장 큰 중량을 든 세트의 반복획수
                    exerciseRecord.setMax_weight(getMaxSetWeight(singleExerciseRecord.getSetInfos()));//exerciseSetRecord의 수행 세트 중 가장 높은 중량 입력
                    exerciseRecord.setTime(singleExerciseRecord.getSetInfos().isEmpty() ? null : singleExerciseRecord.getSetInfos().get(0).getExerciseTime());//exerciseSetRecord의 exerciseTime 입력
                    exerciseRecord.setTotal_exercise_weight(calculateTotalExerciseWeight(singleExerciseRecord.getSetInfos()));
                    exerciseRecord.setDayRecord(dayRecord);

                    List<ExerciseSet> exerciseSets = singleExerciseRecord.getSetInfos().stream()
                            .map(setInfo -> {
                                ExerciseSet exerciseSet = new ExerciseSet();
                                exerciseSet.setDate(LocalDate.now());
                                exerciseSet.setExercise_category(singleExerciseRecord.getExerciseCategory());
                                exerciseSet.setExercise_name(singleExerciseRecord.getExerciseName());
                                exerciseSet.setSet_number(setInfo.getSetNumber());
                                exerciseSet.setRepetitions(setInfo.getRep());
                                exerciseSet.setWeight(setInfo.getSetWeight());
                                exerciseSet.setIsComplete(setInfo.getIsComplete());
                                exerciseSet.setExerciseTime(setInfo.getExerciseTime());
                                exerciseSet.setExerciseRecord(exerciseRecord);
                                return exerciseSet;
                            }).collect(Collectors.toList());
                    exerciseRecord.setExerciseSetList(exerciseSets);
                    return exerciseRecord;
                }).collect(Collectors.toList());

        dayRecord.setTotal_weight(calculateTotalDayWeight(request.getDayExerciseRecord()));
        dayRecord.setCompleteRate(getCompletionRate(request.getDayExerciseRecord()));
        dayRecord.setExerciseRecordList(exerciseRecords);
        dayRecord.setUser(user);

        return dayRecord;
    }

    // 계산식들 isComplete=true인 경우만 하도록 수정

    private static BigDecimal calculateTotalExerciseWeight(List<ExerciseRequest.exerciseSetRecord> setInfos) {
        return setInfos.stream()
                .filter(ExerciseRequest.exerciseSetRecord::getIsComplete)
                .map(setInfo -> setInfo.getSetWeight().multiply(BigDecimal.valueOf(setInfo.getRep())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateTotalDayWeight(List<ExerciseRequest.singleExerciseRecord> exerciseRecords) {
        return exerciseRecords.stream()
                .map(record -> calculateTotalExerciseWeight(record.getSetInfos()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getMaxSetWeight(List<ExerciseRequest.exerciseSetRecord> setInfos) {
        return setInfos.stream()
                .filter(ExerciseRequest.exerciseSetRecord::getIsComplete)
                .map(ExerciseRequest.exerciseSetRecord::getSetWeight)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private static Integer getMaxSetWeightRep(List<ExerciseRequest.exerciseSetRecord> setInfos) {
        return setInfos.stream()
                .filter(ExerciseRequest.exerciseSetRecord::getIsComplete)
                .max(Comparator.comparing(ExerciseRequest.exerciseSetRecord::getSetWeight))
                .map(ExerciseRequest.exerciseSetRecord::getRep)
                .orElse(null);
    }

    private static BigDecimal getCompletionRate(List<ExerciseRequest.singleExerciseRecord> exerciseRecords) {
        long totalSets = exerciseRecords.stream()
                .mapToLong(record -> record.getSetInfos().size())
                .sum();
        long completedSets = exerciseRecords.stream()
                .flatMap(record -> record.getSetInfos().stream())
                .filter(ExerciseRequest.exerciseSetRecord::getIsComplete)
                .count();
        if (totalSets > 0) {
            BigDecimal completionRate = BigDecimal.valueOf(completedSets)
                    .divide(BigDecimal.valueOf(totalSets), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            return completionRate.setScale(2, RoundingMode.HALF_UP); // 소수점 둘째 자리까지 반올림하여 반환
        } else {
            return BigDecimal.ZERO;
        }
    }

}
