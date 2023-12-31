package server.healthyFriends.converter;

import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;

import java.util.Optional;

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

    public static FriendResponse.FriendObjective friendObjective(Optional<Objective> latestObjective, User friend) {
        return FriendResponse.FriendObjective.builder()
                .nickname(friend.getNickname())
                .startDay(latestObjective.map(Objective::getStart_day).orElse(null))
                .endDay(latestObjective.map(Objective::getEnd_day).orElse(null))
                .objectiveBody(latestObjective.map(Objective::getBody).orElse(null))
                .objectiveHead(latestObjective.map(Objective::getHead).orElse(null))
                .build();
    }
}
