package server.healthyFriends.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;


public interface AuthService {
    //UserResponse.JoinResponse join(UserRequest.JoinRequest req);
    UserResponse.TestResponse join(UserRequest.JoinRequest req);

    //UserResponse.LoginResponse login(UserRequest.LoginRequest req);
    UserResponse.TestResponse login(UserRequest.LoginRequest req);

    void logout();
}
