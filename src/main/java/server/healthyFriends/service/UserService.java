package server.healthyFriends.service;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.UserRequest;

public interface UserService {
    boolean checkLoginIdDuplicate(String loginId);
    boolean checkNicknameDuplicate(String nickname);
    void join(UserRequest.JoinRequest req);
    String login(UserRequest.LoginRequest req);

    User getLoginUserById(Long userId);
    User getLoginUserByLoginId(String loginId);

    User getUserById(Long id);
    User findById(Long id);

    User findByLoginId(String loginId);

}
