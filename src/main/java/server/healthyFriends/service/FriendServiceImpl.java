package server.healthyFriends.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.domain.dto.FriendRequest;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구 신청
    public FriendRequest requestFriend(String friend_loginId, Long userId) {

        User requestUser = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 친구 신청 대상자 정보 가져오기
        User recipientUser = userRepository.findByLoginId(friend_loginId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        // FriendMapping 엔티티 생성 및 저장 (나 -> 친구)
        FriendMapping friendMapping = new FriendMapping();
        friendMapping.setUser(requestUser);
        friendMapping.setFriendId(recipientUser.getId());
        friendMapping.setStatus(false); // 초기 상태는 false (미수락 상태)
        friendMapping = friendRepository.save(friendMapping);

        //Request
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester_id(requestUser.getId());
        friendRequest.setRecipient_id(recipientUser.getId());

        return friendRequest;
    }


}