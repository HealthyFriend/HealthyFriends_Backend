package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/{userId}/request")
    public ResponseDTO<FriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody FriendRequest friendRequest) {

            User requestUser = userService.findById(userId);

            User recipientUser = userService.findByLoginId(friendRequest.getRecipient_loginId());

            FriendResponse friendResponse = friendService.requestFriend(userId, friendRequest.getRecipient_loginId());

            return ResponseUtil.success("친구 신청에 성공했습니다.",friendResponse);

    }
}
