package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


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
}
