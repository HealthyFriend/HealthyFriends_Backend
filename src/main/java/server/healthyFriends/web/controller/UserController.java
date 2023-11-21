package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.domain.User;
import server.healthyFriends.domain.dto.JoinRequest;
import server.healthyFriends.domain.dto.LoginRequest;
import server.healthyFriends.domain.dto.ResponseDTO;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.service.UserService;
import server.healthyFriends.web.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private String secretKey;
    @PostMapping("/join")
    public ResponseDTO<String> join(@RequestBody JoinRequest joinRequest) {
        try {
            //loginId 중복 체크
            if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
                return ResponseUtil.conflict("이미 존재하는 아이디입니다.", null);
            }

            if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
                return ResponseUtil.conflict("이미 존재하는 닉네임입니다.",null);
            }

            if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
                return ResponseUtil.badRequest("비밀번호와 비밀번호 확인이 일치하지 않습니다.",null);
            }

            userService.join(joinRequest);
            return ResponseUtil.success("회원가입에 성공하셨습니다.",null);

        } catch(Exception e) {
            throw e;
        }
    }



    @PostMapping("/login")
    public ResponseDTO<String> login(@RequestBody LoginRequest loginRequest) {

        User user = userService.login(loginRequest);

        try {

            // 로그인 아이디나 비밀번호가 틀린 경우 global error return
            if (user == null) {
                return ResponseUtil.unauthorized("로그인 아이디 또는 비밀번호가 틀렸습니다.", null);
            }

            // 로그인 성공 => Jwt Token 발급

            //String secretKey = "my-secret-key-20220121";
            long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

            String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

            return ResponseUtil.success("로그인 성공", jwtToken);

        } catch(Exception e) {
            throw e;
        }
    }

    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
}
