package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.ion.Decimal;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;


public class ExerciseRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class addExerciseRequest {
        @Schema(description = "운동 부위", nullable = false, example = "가슴")
        private String exerciseCategory;
        @Schema(description = "운동 이름", nullable = false, example = "벤치프레스")
        private String exerciseName;
        @Schema(description = "운동 분류", nullable = true, example = "null")
        private Boolean withWeight;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class exerciseSetRecord {
        @Schema(description = "세트 넘버", nullable = true, example = "1")
        private Integer setNumber;
        @DecimalMin(value="0") @DecimalMax(value="200")
        @Schema(description = "세트 중량", nullable = true, example = "60")
        private BigDecimal setWeight;
        @Schema(description = "해당 세트의 수행 횟수", nullable = true, example = "12")
        private Integer rep;
        // 유산소 운동의 경우는 수행 시간, 수행 여부만 필요
        @Schema(description = "유산소 운동 수행 시간",nullable = true,example = "유산소 ex) 00:40:00")
        private LocalTime exerciseTime;
        @Schema(description = "해당 세트의 수행 여부", nullable = true,example = "true")
        private Boolean isComplete;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class singleExerciseRecord {
        @Schema(description = "운동 부위(or 유산소)", nullable = false, example = "가슴")
        private String exerciseCategory;
        @Schema(description = "운동 이름", nullable = false, example = "벤치프레스")
        private String exerciseName;
        // 유산소 등록하기 전까진 true
        @Schema(description = "운동 분류(웨이트 or 유산소)", nullable = true, example = "null")
        private Boolean withWeight;
        // withWeight 값에 따라 변동
        @Schema(description ="해당 운동 총 볼륨(중량)",nullable = true,example = "0")
        private BigDecimal exerciseTotalWeight;
        @Schema(description ="운동 수행 내역",nullable = false)
        private List<exerciseSetRecord> setInfos;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class exerciseRecordRequest {
    @Schema(description = "하루 운동 수행 내역")
    private List<singleExerciseRecord> dayExerciseRecord;
    @Schema(description = "하루 운동 시간",nullable = true,example = "01:12:10")
    private LocalTime dayExerciseTime;
    @Schema(description = "하루 총 볼륨(중량)",nullable = true,example = "0")
    private BigDecimal dayTotalWeight;
    @Schema(description = "하루 운동 달성률",nullable = true,example = "87.14")
    private BigDecimal completionRate;

    }
}
