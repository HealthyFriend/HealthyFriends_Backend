package server.healthyFriends.web.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ObjectiveResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateObjectiveResponse {
        private Long objectiveId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleObjectiveResponse {
        private LocalDate start_Day;
        private LocalDate end_Day;
        private String head;
        private String body;
        private Boolean status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListObjectiveResponse {
        List<ObjectiveResponse.SingleObjectiveResponse> objectiveList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateObjectiveResultDTO {
        Long objectiveId;
        LocalDateTime createdAt;
    }


}
