package server.healthyFriends.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
