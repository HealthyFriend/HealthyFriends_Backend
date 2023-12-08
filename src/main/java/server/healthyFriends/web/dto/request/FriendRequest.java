package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class FriendRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestFriendRequest {
        @Schema(description = "친구 신청 대상의 로그인ID", nullable = false, example = "하은@example.com")
        private String recipientLoginId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AcceptFriendRequest {
        @Schema(description = "friend_mapping_id", nullable = false, example = "1")
        private Long friendMappingId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RejectFriendRequest {
        @Schema(description = "friend_mapping_id", nullable = false, example = "1")
        private Long friendMappingId;
    }

}
