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

        //친구 요청 성공 시 반환 DTO
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester_id(requestUser.getId());
        friendRequest.setRecipient_id(recipientUser.getId());
        friendRequest.setId(friendMapping.getId());

        return friendRequest;
    }

    // 친구 수락
    public void acceptFriend(Long friendMappingId, Long request_userId, Long recipient_userId) {

        User requestUser = userRepository.findById(request_userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        User recipientUser = userRepository.findById(recipient_userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));


        FriendMapping friendMapping = friendRepository.findById(friendMappingId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        // 친구 상태 true로 변환
        friendMapping.setStatus(true);
        friendRepository.save(friendMapping);

        // 역순으로 테이블에 하나 더 저장
        FriendMapping friendMappingReverse = new FriendMapping();
        friendMappingReverse.setUser(recipientUser);
        friendMappingReverse.setFriendId(requestUser.getId());
        friendMappingReverse.setStatus(true);
        friendRepository.save(friendMappingReverse);
    }

    // 친구 거절
    public void rejectFriend(Long friendMappingId) {

        FriendMapping obsoleteFriendMapping = friendRepository.findById(friendMappingId)
                        .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        friendRepository.delete(obsoleteFriendMapping);
    }
}