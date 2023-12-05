package server.healthyFriends.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

    @PostMapping("/join")
    public ResponseDTO<String> join(@RequestBody @Valid UserRequest.JoinRequest joinRequest) {
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

    }



    @PostMapping("/login")
    public ResponseDTO<String> login(@RequestBody @Valid UserRequest.LoginRequest loginRequest) {

        String accessToken = userService.login(loginRequest);

        return ResponseUtil.success("로그인 성공", accessToken);

    }

    @DeleteMapping("/{userId}")
    public ResponseDTO<String> withdrawal(@RequestBody @Valid UserRequest.WithdrawalRequest req,
                                          @PathVariable("userId") Long userId) {

        userService.withdrawal(userId, req);

        return ResponseUtil.success("회원 탈퇴 성공",null);
    }

    @PutMapping("/{userId}")
    private ResponseDTO<String> modifyUserInfo(@RequestBody @Valid UserRequest.ModifyUserInfoRequest req,
                                               @PathVariable("userId") Long userId) {
        userService.modifyUserInfo(userId,req);

        return ResponseUtil.success("회원 정보 수정 성공",null);
    }

    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
}
