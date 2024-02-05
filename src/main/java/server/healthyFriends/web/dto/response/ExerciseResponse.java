package server.healthyFriends.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.healthyFriends.domain.entity.DayRecord;
import server.healthyFriends.domain.entity.ExerciseRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ExerciseResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addExerciseResponse {
        private Long exerciseId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class singleExercise {
        private String exerciseName;
        private String exerciseCategory;
        private Boolean withWeight;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getExerciseResponse {
        List<ExerciseResponse.singleExercise> exerciseList;
        //Integer listSize;
        //Integer totalPage;
        //Long totalElements;
        //Boolean isFirst;
        //Boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addExerciseDayRecordResponse {
        private Long dayExerciseRecordId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getExerciseDayRecordResponse {
        private List<SingleExerciseRecordResponse> dayExerciseRecord;
        private LocalTime dayExerciseTime;
        private BigDecimal dayTotalWeight;
        private BigDecimal completionRate;
        private Long dayRecordId;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleExerciseRecordResponse {
        private String exerciseCategory;
        private String exerciseName;
        private Boolean withWeight;
        private BigDecimal exerciseTotalWeight;
        private List<ExerciseSetRecordResponse> setInfos;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseSetRecordResponse {
        private Integer setNumber;
        private BigDecimal setWeight;
        private Integer rep;
        private LocalTime exerciseTime;
        private Boolean isComplete;
    }
}
