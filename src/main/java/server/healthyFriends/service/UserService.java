package server.healthyFriends.service;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

public interface UserService {
    void withdrawal(Long id, UserRequest.WithdrawalRequest req);
    void modifyUserInfo(Long userId, UserRequest.ModifyUserInfoRequest req);
}
