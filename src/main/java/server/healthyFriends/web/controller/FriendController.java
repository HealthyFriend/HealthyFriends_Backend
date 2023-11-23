package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.domain.dto.FriendRequest;
import server.healthyFriends.domain.dto.FriendResponse;
import server.healthyFriends.domain.dto.ObjectiveRequest;
import server.healthyFriends.domain.dto.ResponseDTO;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.FriendService;
import server.healthyFriends.web.response.ResponseUtil;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    // 친구 신청
    @PostMapping("/{userId}/requset")
    public ResponseDTO<FriendResponse> requestFriend(
            @PathVariable("userId") Long userId,
            @RequestBody FriendRequest friendRequest) {

        try {

            User requestUser = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

            User recipientUser = userRepository.findByLoginId(friendRequest.getRecipient_loginId())
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

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
