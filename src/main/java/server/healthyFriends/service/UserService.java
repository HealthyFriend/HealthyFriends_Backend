package server.healthyFriends.service;

import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.dto.JoinRequest;
import server.healthyFriends.domain.dto.LoginRequest;

public interface UserService {
    public boolean checkLoginIdDuplicate(String loginId);
    public boolean checkNicknameDuplicate(String nickname);
    public void join(JoinRequest req);
    public User login(LoginRequest req);

    public User getLoginUserById(Long userId);
    public User getLoginUserByLoginId(String loginId);

    public User getUserById(Long id);

}
