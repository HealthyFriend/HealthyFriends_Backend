package server.healthyFriends.service;

import server.healthyFriends.domain.dto.FriendResponse;

public interface FriendService {
    public FriendResponse requestFriend(Long userId, String friend_loginId);

    public void acceptFriend(Long friendMappingId, Long request_userId, Long recipient_userId);

    public void rejectFriend(Long friendMappingId);
}
