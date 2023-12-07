package server.healthyFriends.converter;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;

public class FriendConverter {

    public static FriendResponse.FindFriendResponse findFriendResponse(User friendUser) {
        return FriendResponse.FindFriendResponse.builder()
                .friendLoginId(friendUser.getLoginId())
                .friendName(friendUser.getName())
                .friendNickname(friendUser.getNickname())
                .build();
    }
    public static FriendResponse.RequestFriendResponse requestFrinedResponse(Long requestUserId, Long recipientUserId, Long mappingFriendId) {
        return FriendResponse.RequestFriendResponse.builder()
                .requesterId(requestUserId)
                .recipientId(recipientUserId)
                .friendMappingId(mappingFriendId)
                .build();
    }

    public static FriendResponse.AcceptFriendResponse acceptFriendResponse(Long requestUserId, Long recipientUserId, Long mappingFriendId) {
        return FriendResponse.AcceptFriendResponse.builder()
                .requesterId(requestUserId)
                .recipientId(recipientUserId)
                .friendMappingId(mappingFriendId)
                .build();
    }

    public static FriendResponse.FriendInfo friendInfo(String nickname, String objecviteHead) {
        return FriendResponse.FriendInfo.builder()
                .nickname(nickname)
                .objectiveHead(objecviteHead)
                .build();
    }
}
