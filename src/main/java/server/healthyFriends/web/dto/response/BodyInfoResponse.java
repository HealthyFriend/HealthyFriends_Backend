package server.healthyFriends.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BodyInfoResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBodyInfoResponse {
        private Long bodyInfoCompositionRecordId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBodyInfoResponse {
        private BigDecimal weight;
        private BigDecimal skeletal_muscle_mass;
        private BigDecimal body_fat_mass;
        private LocalDate edit_day;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeightChange {
        private LocalDate date;
        private BigDecimal weight;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWeightChange {
        private Optional<List<BodyInfoResponse.WeightChange>> dailyWeightList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyWeightChange {
        private Optional<List<BodyInfoResponse.WeightChange>> monthlyWeightList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MuscleChange {
        private LocalDate date;
        private BigDecimal muscle;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyMuscleChange {
        private Optional<List<BodyInfoResponse.MuscleChange>> dailyMuscleList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyMuscleChange {
        private Optional<List<BodyInfoResponse.MuscleChange>> monthlyMuscleList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FatChange {
        private LocalDate date;
        private BigDecimal fat;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyFatChange {
        private Optional<List<BodyInfoResponse.FatChange>> dailyFatList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyFatChange {
        private Optional<List<BodyInfoResponse.FatChange>> monthlyFatList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BmiChange {
        private LocalDate date;
        private BigDecimal bmi;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyBmiChange {
        private Optional<List<BodyInfoResponse.BmiChange>> dailyBmiList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyBmiChange {
        private Optional<List<BodyInfoResponse.BmiChange>> monthlyBmiList;
    }
}
