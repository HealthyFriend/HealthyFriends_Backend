package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;


@Tag(name="FriendController",description = "기능 구분 : 친구")
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @Operation(summary = "아이디로 친구 찾기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{friendLoginId}")
    public ResponseDTO<FriendResponse.FindFriendResponse> findFriend(@RequestParam(name = "friendLoginId") String friendLoginId) {

        FriendResponse.FindFriendResponse findFriendResponse = friendService.findFriendResponse(friendLoginId);

        return ResponseUtil.success("친구 찾기 성공", findFriendResponse);
    }

    // 친구 신청
    @Operation(summary = "친구 신청")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/{userId}/request")
    public ResponseDTO<FriendResponse.RequestFriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid FriendRequest.RequestFriendRequest requestFriendRequest) {

        FriendResponse.RequestFriendResponse friendResponse = friendService.requestFriend(userId, requestFriendRequest);

        return ResponseUtil.success("친구 신청에 성공했습니다.", friendResponse);

    }

    @Operation(summary = "친구 요청 수락")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/accept")
    public ResponseDTO<FriendResponse.AcceptFriendResponse> acceptFriend (
            @RequestBody @Valid FriendRequest.AcceptFriendRequest acceptFriendRequest) {

        FriendResponse.AcceptFriendResponse acceptFriendResponse = friendService.acceptFriend(acceptFriendRequest);

        return ResponseUtil.success("친구 요청을 수락했습니다.",acceptFriendResponse);
    }

    @Operation(summary = "친구 요청 거절")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })

    @PostMapping("/reject")
    public ResponseDTO<String> rejectFriend (
            @RequestBody @Valid FriendRequest.RejectFriendRequest rejectFriendRequest) {

        friendService.rejectFriend(rejectFriendRequest);

        return ResponseUtil.success("친구 거절 성공",null);
    }

    @Operation(summary = "친구 목표 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{friendId}/objective")
    public ResponseDTO<FriendResponse.FriendObjective> readFriendObjective(@PathVariable("friendId")Long friendId) {

        FriendResponse.FriendObjective friendObjective = friendService.readFriendObjective(friendId);

        return ResponseUtil.success("친구 목표 조회 성공",friendObjective);
    }


}
