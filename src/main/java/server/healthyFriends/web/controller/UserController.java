package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.FriendResponse;

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
    @DeleteMapping("/{userId}")
    public ResponseDTO<String> withdrawal(@RequestBody @Valid UserRequest.WithdrawalRequest req,
                                          @PathVariable("userId") Long userId) {

        userService.withdrawal(userId, req);

        return ResponseUtil.success("회원 탈퇴 성공",null);
    }

    @Operation(summary = "회원 정보 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/{userId}")
    private ResponseDTO<String> modifyUserInfo(@RequestBody @Valid UserRequest.ModifyUserInfoRequest req,
                                               @PathVariable("userId") Long userId) {
        userService.modifyUserInfo(userId,req);

        return ResponseUtil.success("회원 정보 수정 성공",null);
    }

    // 친구 신청
    @Operation(summary = "친구 신청")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("{userId}/friend-request")
    public ResponseDTO<FriendResponse.RequestFriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid FriendRequest.RequestFriendRequest requestFriendRequest) {

        FriendResponse.RequestFriendResponse friendResponse = friendService.requestFriend(userId, requestFriendRequest);

        return ResponseUtil.success("친구 신청에 성공했습니다.", friendResponse);

    }

    @Operation(summary = "친구 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseDTO<String> deleteFriend (@PathVariable("userId") Long userId,
                                             @PathVariable("friendId") Long friendId) {
        friendService.deleteFriend(userId,friendId);

        return ResponseUtil.success("친구 삭제 성공",null);
    }

    @Operation(summary = "친구 리스트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{userId}")
    public ResponseDTO<FriendResponse.ListFriendResponse> readFriends(@PathVariable("userId") Long userId) {

        FriendResponse.ListFriendResponse listFriendResponse = userService.readFriends(userId);

        return ResponseUtil.success("친구 리스트 조회 성공",listFriendResponse);
    }


    @Operation(summary = "안씀")
    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
}
