package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.web.dto.response.UserResponse;

import java.util.Optional;

@Tag(name="UserController",description = "기능 구분 : 회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;
    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원 탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @DeleteMapping("/withdrawal")
    public ResponseDTO<String> withdrawal(@RequestBody @Valid UserRequest.WithdrawalRequest req,
                                          Authentication authentication) {

        Long userId=Long.parseLong(authentication.getName());

        userService.withdrawal(userId, req);

        return ResponseUtil.success("회원 탈퇴 성공",null);
    }

    @Operation(summary = "회원 정보 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/edit-profile")
    public ResponseDTO<UserResponse.UserInfoResponse> modifyUserInfo(@RequestBody @Valid UserRequest.ModifyUserInfoRequest req,
                                               Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        UserResponse.UserInfoResponse userInfoResponse = userService.modifyUserInfo(userId,req);

        return ResponseUtil.success("회원 정보 수정 성공",userInfoResponse);
    }

    @Operation(summary = "회원 정보(프로필) 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/profile")
    public ResponseDTO<Optional<UserResponse.UserInfoResponse>> getUserInfo(Authentication authentication) {

        User user = userService.getUser(Long.parseLong(authentication.getName()));

        Optional<UserResponse.UserInfoResponse> userInfoResponse = userService.getUserInfo(user.getId());

        return ResponseUtil.success("회원 정보 조회 성공",userInfoResponse);
    }

    // 친구 신청
    @Operation(summary = "친구 신청")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/friend-request")
    public ResponseDTO<FriendResponse.RequestFriendResponse> requestFriend(
            @RequestBody @Valid FriendRequest.RequestFriendRequest requestFriendRequest,
            Authentication authentication) {

        Long userId=Long.parseLong(authentication.getName());

        FriendResponse.RequestFriendResponse friendResponse = friendService.requestFriend(userId, requestFriendRequest);

        return ResponseUtil.success("친구 신청에 성공했습니다.", friendResponse);

    }

    @Operation(summary = "친구 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @DeleteMapping("/friends/{friendId}")
    public ResponseDTO<String> deleteFriend (Authentication authentication,
                                             @PathVariable("friendId") Long friendId) {
        Long userId=Long.parseLong(authentication.getName());

        friendService.deleteFriend(userId,friendId);

        return ResponseUtil.success("친구 삭제 성공",null);
    }

    @Operation(summary = "친구 리스트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends")
    public ResponseDTO<FriendResponse.ListFriendResponse> readFriends(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        FriendResponse.ListFriendResponse listFriendResponse = userService.readFriends(userId);

        return ResponseUtil.success("친구 리스트 조회 성공",listFriendResponse);
    }


    @Operation(summary = "안씀")
    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
}
