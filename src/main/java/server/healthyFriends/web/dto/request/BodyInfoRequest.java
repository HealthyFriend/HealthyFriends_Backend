package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BodyInfoRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateBodyInfoRequest {

        @DecimalMin(value="30.0") @DecimalMax(value="150.0")
        @Schema(description = "몸무게", nullable = true, example = "65.0")
        private BigDecimal weight;

        @DecimalMin(value="5.0") @DecimalMax(value="90.0")
        @Schema(description = "골격근량", nullable = true, example = "30.0")
        private BigDecimal skeletal_muscle_mass;

        @DecimalMin(value="5.0") @DecimalMax(value="90.0")
        @Schema(description = "체지방", nullable = true, example = "20.0")
        private BigDecimal body_fat_mass;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateBodyInfoRequest {

        @DecimalMin(value="30.0") @DecimalMax(value="150.0")
        @Schema(description = "몸무게", nullable = true, example = "70.0")
        private BigDecimal weight;

        @DecimalMin(value="5.0") @DecimalMax(value="90.0")
        @Schema(description = "골격근량", nullable = true, example = "35.0")
        private BigDecimal skeletal_muscle_mass;

        @DecimalMin(value="5.0") @DecimalMax(value="90.0")
        @Schema(description = "체지방", nullable = true, example = "25.0")
        private BigDecimal body_fat_mass;

        @Schema(description = "수정할 날짜", nullable = false, example = "2023-12-07")
        private LocalDate edit_day;
    }
}
