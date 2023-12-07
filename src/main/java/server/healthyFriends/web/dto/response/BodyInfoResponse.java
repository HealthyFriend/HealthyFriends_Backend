package server.healthyFriends.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BodyInfoResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBodyInfoResponse {
        private Long bodyInfoCompositionRecordId;
    }
}
