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
import org.springframework.web.bind.annotation.RestController;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.service.auth.AuthService;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

@Tag(name="AuthController",description = "기능 구분 : 인증")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

    @Operation(summary = "로그아웃")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/logout")
    public ResponseDTO<String> logout() {
        authService.logout();
        return ResponseUtil.success("로그아웃 성공",null);
    }

}
