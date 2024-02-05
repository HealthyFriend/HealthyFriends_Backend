package server.healthyFriends.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    public static class addExerciseRecordResponse {
        private Long dayExerciseRecordId;
    }


}
