package server.healthyFriends.converter;

import server.healthyFriends.web.dto.response.FriendResponse;

public class FriendConverter {

    public static FriendResponse.RequestFriendResponse requestFrinedResponse(Long requestUserId, Long recipientUserId, Long mappingFriendId) {
        return FriendResponse.RequestFriendResponse.builder()
                .requester_id(requestUserId)
                .recipient_id(recipientUserId)
                .friend_mapping_id(mappingFriendId)
                .build();
    }
}
