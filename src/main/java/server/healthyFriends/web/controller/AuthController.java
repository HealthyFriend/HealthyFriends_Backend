package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.sercurity.OAuth.CustomOauth2UserService;
import server.healthyFriends.sercurity.OAuth.Naver.NaverLoginParams;
import server.healthyFriends.service.auth.AuthService;
import server.healthyFriends.service.auth.AuthTokens;
import server.healthyFriends.service.auth.OAuthLoginService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

@Tag(name="AuthController",description = "기능 구분 : 인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthLoginService oAuthLoginService;
    private final CustomOauth2UserService customOauth2UserService;
/*
    본래 회원가입
    @Operation(summary = "회원가입")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/join")
    public ResponseDTO<UserResponse.JoinResponse> join(@RequestBody @Valid UserRequest.JoinRequest joinRequest) {


        UserResponse.JoinResponse joinResponse = authService.join(joinRequest);
        return ResponseUtil.created("회원가입에 성공하셨습니다.",joinResponse);

    }
*/
    @Operation(summary = "회원가입")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/join")
    public ResponseDTO<UserResponse.TestResponse> join(@RequestBody @Valid UserRequest.JoinRequest joinRequest) {

        UserResponse.TestResponse joinResponse = authService.join(joinRequest);
        return ResponseUtil.created("회원가입에 성공하셨습니다.",joinResponse);

    }

/*
    본래 로그인
    @Operation(summary = "로그인")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/login")
    public ResponseDTO<UserResponse.LoginResponse> login(@RequestBody @Valid UserRequest.LoginRequest loginRequest) {

        UserResponse.LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseUtil.success("로그인 성공", loginResponse);

    }
*/

    @Operation(summary = "로그인")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/login")
    public ResponseDTO<UserResponse.TestResponse> login(@RequestBody @Valid UserRequest.LoginRequest loginRequest) {

        UserResponse.TestResponse loginResponse = authService.login(loginRequest);

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
        authService.logout();
        return ResponseUtil.success("로그아웃 성공","success");
    }

    @Operation(summary = "네이버 로그인")
    @PostMapping("/login/oauth2/naver")
    public ResponseDTO<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
        return null;
    }

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login/oauth2/kakao")
    public ResponseEntity<UserResponse.LoginResponse> loginKakao() {
        return null;
    }




}
