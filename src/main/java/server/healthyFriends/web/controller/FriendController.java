package server.healthyFriends.web.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    // 친구 신청
    @PostMapping("/{userId}/requset")
    public ResponseDTO<FriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody FriendRequest friendRequest) {

        try {

            User requestUser = userService.findById(userId);

            User recipientUser = userService.findByLoginId(friendRequest.getRecipient_loginId());

            FriendResponse friendResponse = friendService.requestFriend(userId, friendRequest.getRecipient_loginId());

            return ResponseUtil.success("친구 신청에 성공했습니다.",friendResponse);

        } catch (EntityNotFoundException e) {
            return ResponseUtil.notFound("해당하는 유저가 없습니다.", null);
        } catch (IllegalStateException e) {
            return ResponseUtil.badRequest("친구 요청중입니다.",null);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
