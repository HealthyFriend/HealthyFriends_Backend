package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.healthyFriends.S3.S3Service;
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

import java.util.List;
import java.util.Optional;

@Tag(name="UserController",description = "기능 구분 : 회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;
    private final S3Service s3Service;
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

    @Operation(summary = "로그인 아이디(이메일)로 유저 찾기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 유저 찾기 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friend-loginId/{friendLoginId}")
    public ResponseDTO<FriendResponse.FindFriendResponse> findFriendByLoginId(@PathVariable(name = "friendLoginId") String friendLoginId) {

        FriendResponse.FindFriendResponse findFriendResponse = friendService.findFriendbyLoginId(friendLoginId);

        return ResponseUtil.success("친구 찾기 성공", findFriendResponse);
    }

    @Operation(summary = "닉네임으로 유저 찾기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 유저 찾기 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friend-nickname/{friendNickname}")
    public ResponseDTO<FriendResponse.FindFriendResponse> findFriendByNickname(@PathVariable(name = "friendNickname") String friendNickname) {

        FriendResponse.FindFriendResponse findFriendResponse = friendService.findFriendbyNickname(friendNickname);

        return ResponseUtil.success("친구 찾기 성공", findFriendResponse);
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

    @Operation(summary = "친구 매핑 현황 조회(나의 친구+내가 요청 보낸 사람)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 매핑 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })

    @GetMapping("/friend-mapping")
    public ResponseDTO<Optional<List<FriendResponse.MappingFriendResponse>>> mappingFriend (Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        return ResponseUtil.success("친구 매핑 조회 성공",friendService.mappingFriend(userId));
    }

    @Operation(summary = "받은 친구 요청 현황 목록 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 받은 친구 요청 현황 목록 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })

    @GetMapping("/prospective-friend")
    public ResponseDTO<Optional<List<FriendResponse.MappingFriendResponse>>> prospectiveFriend (Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        return ResponseUtil.success("받은 친구 요청 현황 목록 조회",friendService.prospectiveFriend(userId,false));
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

    @Operation(summary = "프로필 이미지 업로드")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 프로필 이미지 업로드 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping(path= "/profile-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<String> uploadProfileImage(@RequestPart(value = "file", required = false) MultipartFile file,
                                                  Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("프로필 이미지 업로드 성공",userService.uploadProfileImage(userId,file));
    }

    @Operation(summary = "프로필 이미지 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 프로필 이미지 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping(path= "/profile-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<String> editProfileImage(@RequestPart(value = "file", required = false) MultipartFile file,
                                                  Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("프로필 이미지 업로드 성공",userService.editProfileImage(userId,file));
    }
/*
    @Operation(summary = "X")
    @GetMapping("/admin")
    public ResponseDTO<String> adminPage() {
        return ResponseUtil.success("관리자 페이지 접근",null);
    }
*/
}

