package server.healthyFriends.service;

import server.healthyFriends.domain.User;
import server.healthyFriends.domain.dto.LoginRequest;

public interface UserService {
    public boolean checkLoginIdDuplicate(String loginId);
    public User login(LoginRequest req);

    public User getLoginUserById(Long userId);
    public User getLoginUserByLoginId(String loginId);

}
