package server.healthyFriends.service;

import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;

public interface FriendService {
    FriendResponse.RequestFriendResponse requestFriend(Long userId, FriendRequest.RequestFriendDTO requestFriendDTO);

    void acceptFriend(Long friendMappingId, Long request_userId, Long recipient_userId);

    void rejectFriend(Long friendMappingId);
}
