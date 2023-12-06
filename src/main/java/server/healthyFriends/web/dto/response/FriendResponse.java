package server.healthyFriends.web.dto.response;

import lombok.*;


public class FriendResponse {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestFriendResponse {
        private Long friend_mapping_id;

        private Long requester_id;

        private Long recipient_id;
    }

}
