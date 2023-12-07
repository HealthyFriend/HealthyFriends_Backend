package server.healthyFriends.service;

import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;

public interface FriendService {

    FriendResponse.FindFriendResponse findFriendResponse(String friendLoginId);
    FriendResponse.RequestFriendResponse requestFriend(Long userId, FriendRequest.RequestFriendRequest requestFriendRequest);

    FriendResponse.AcceptFriendResponse acceptFriend(FriendRequest.AcceptFriendRequest acceptFriendRequest);

    void rejectFriend(FriendRequest.RejectFriendRequest rejectFriendRequest);
}
