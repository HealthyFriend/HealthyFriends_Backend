package server.healthyFriends.service;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.JoinRequest;
import server.healthyFriends.web.dto.LoginRequest;

public interface UserService {
    boolean checkLoginIdDuplicate(String loginId);
    boolean checkNicknameDuplicate(String nickname);
    void join(JoinRequest req);
    String login(LoginRequest req);

    User getLoginUserById(Long userId);
    User getLoginUserByLoginId(String loginId);

    User getUserById(Long id);
    User findById(Long id);

    User findByLoginId(String loginId);

}
