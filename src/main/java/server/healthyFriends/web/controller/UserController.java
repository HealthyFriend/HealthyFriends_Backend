package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.web.dto.request.UserRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public ResponseDTO<String> join(@RequestBody UserRequest.JoinRequest joinRequest) {
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
            return ResponseUtil.created("회원가입에 성공하셨습니다.",null);

        } catch(Exception e) {
            throw e;
        }
    }



    @PostMapping("/login")
    public ResponseDTO<String> login(@RequestBody UserRequest.LoginRequest loginRequest) {

        String accessToken = userService.login(loginRequest);

        try {

            return ResponseUtil.success("로그인 성공", accessToken);

        } catch(Exception e) {
            throw e;
        }
    }

    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
}
