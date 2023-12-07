package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.service.UserService;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

@Tag(name="AuthController",description = "기능 구분 : 인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
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


    @Operation(summary = "로그인")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/login")
    public ResponseDTO<UserResponse.LoginResponse> login(@RequestBody @Valid UserRequest.LoginRequest loginRequest) {

        UserResponse.LoginResponse loginResponse = userService.login(loginRequest);

        return ResponseUtil.success("로그인 성공", loginResponse);

    }

    @Operation(summary = "로그아웃")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/logout")
    public ResponseDTO<String> logout() {
        userService.logout();
        return ResponseUtil.success("로그아웃 성공",null);
    }

}
