package server.healthyFriends.service;

import server.healthyFriends.domain.dto.FriendRequest;

public interface FriendService {
    public FriendRequest requestFriend(String friend_loginId, Long userId);
}
