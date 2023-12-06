package server.healthyFriends.service;

import server.healthyFriends.web.dto.response.FriendResponse;

public interface FriendService {
    FriendResponse.RequestFriendResponse requestFriend(Long userId, String friend_loginId);

    void acceptFriend(Long friendMappingId, Long request_userId, Long recipient_userId);

    void rejectFriend(Long friendMappingId);
}
