package server.healthyFriends.service.user;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.web.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    User getUser(Long userId);
    void withdrawal(Long id, UserRequest.WithdrawalRequest req);
    UserResponse.UserInfoResponse modifyUserInfo(Long userId, UserRequest.ModifyUserInfoRequest req);
    FriendResponse.ListFriendResponse readFriends(Long userId);
    Optional<UserResponse.UserInfoResponse> getUserInfo(Long userId);
}
