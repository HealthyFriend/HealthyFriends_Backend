package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.service.FriendService;
import server.healthyFriends.service.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;


@Tag(name="FriendController",description = "기능 구분 : 친구")
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @Operation(summary = "아이디로 친구 찾기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{friendLoginId}")
    public ResponseDTO<FriendResponse.FindFriendResponse> findFriend(@RequestParam(name="friendLoginId") String friendLoginId) {

        FriendResponse.FindFriendResponse findFriendResponse = friendService.findFriendResponse(friendLoginId);

        return ResponseUtil.success("친구 찾기 성공", findFriendResponse);
    }

    // 친구 신청
    @Operation(summary = "친구 신청")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/{userId}/request")
    public ResponseDTO<FriendResponse.RequestFriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid FriendRequest.RequestFriendDTO requestFriendDTO) {

            FriendResponse.RequestFriendResponse friendResponse = friendService.requestFriend(userId, requestFriendDTO);

            return ResponseUtil.success("친구 신청에 성공했습니다.",friendResponse);

    }
}
