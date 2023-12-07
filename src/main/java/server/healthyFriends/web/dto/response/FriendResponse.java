package server.healthyFriends.web.dto.response;

import lombok.*;


public class FriendResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindFriendResponse {
        private String friendLoginId;



    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestFriendResponse {
        private Long friendMappingId;

        private Long requesterId;

        private Long recipientId;
    }

}
