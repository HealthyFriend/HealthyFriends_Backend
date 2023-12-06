package server.healthyFriends.service;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

public interface UserService {
    boolean checkLoginIdDuplicate(String loginId);
    boolean checkNicknameDuplicate(String nickname);
    void join(UserRequest.JoinRequest req);
    UserResponse.LoginResponse login(UserRequest.LoginRequest req);
    void logout();
    User getLoginUserById(Long userId);
    User getLoginUserByLoginId(String loginId);

    User getUserById(Long id);
    User findById(Long id);
    User findByLoginId(String loginId);
    void withdrawal(Long id, UserRequest.WithdrawalRequest req);
    void modifyUserInfo(Long userId, UserRequest.ModifyUserInfoRequest req);
}
