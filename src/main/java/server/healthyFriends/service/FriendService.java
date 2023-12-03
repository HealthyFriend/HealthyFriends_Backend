package server.healthyFriends.service;

import server.healthyFriends.web.dto.FriendResponse;

public interface FriendService {
    FriendResponse requestFriend(Long userId, String friend_loginId);

    void acceptFriend(Long friendMappingId, Long request_userId, Long recipient_userId);

    void rejectFriend(Long friendMappingId);
}
