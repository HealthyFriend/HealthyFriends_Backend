package server.healthyFriends.converter;

import server.healthyFriends.web.dto.response.FriendResponse;

public class FriendConverter {

    public static FriendResponse.RequestFriendResponse requestFrinedResponse(Long requestUserId, Long recipientUserId, Long mappingFriendId) {
        return FriendResponse.RequestFriendResponse.builder()
                .requesterId(requestUserId)
                .recipientId(recipientUserId)
                .friendMappingId(mappingFriendId)
                .build();
    }
}
