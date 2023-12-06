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
            @RequestBody @Valid FriendRequest friendRequest) {

            User requestUser = userService.findById(userId);

            User recipientUser = userService.findByLoginId(friendRequest.getRecipient_loginId());

            FriendResponse.RequestFriendResponse friendResponse = friendService.requestFriend(userId, friendRequest.getRecipient_loginId());

            return ResponseUtil.success("친구 신청에 성공했습니다.",friendResponse);

    }
}
