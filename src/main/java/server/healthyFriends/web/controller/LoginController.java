package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.config.AppConfig;
import server.healthyFriends.domain.User;
import server.healthyFriends.domain.dto.LoginRequest;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.service.UserService;
import server.healthyFriends.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 => Jwt Token 발급

        String secretKey = "my-secret-key-20220121";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        return jwtToken;
    }
}
