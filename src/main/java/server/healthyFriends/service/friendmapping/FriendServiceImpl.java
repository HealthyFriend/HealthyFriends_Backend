package server.healthyFriends.service.friendmapping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.FriendConverter;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // LoginId로 친구 찾기
    public FriendResponse.FindFriendResponse findFriendbyLoginId(String friendLoginId) {

        User friendUser = userRepository.findByLoginId(friendLoginId)
                .orElseThrow(()-> new EntityNotFoundException("해당하는 유저가 없습니다."));

        return FriendConverter.findFriendResponse(friendUser);
    }



    // 친구 신청
    public FriendResponse.RequestFriendResponse requestFriend(Long userId, FriendRequest.RequestFriendRequest requestFriendRequest) {

        User requestUser = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 친구 신청 대상자 정보 가져오기
        User recipientUser = userRepository.findByLoginId(requestFriendRequest.getRecipientLoginId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 이미 친구 신청이 있는지 확인
        if (friendRepository.existsByUserIdAndFriendIdAndStatus(requestUser.getId(), recipientUser.getId(), false)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"친구 신청 중입니다.");
        }

        // 본인에게 친구 신청 불가능
        if(requestUser==recipientUser) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"본인에게 친구 신청은 불가합니다.");
        }

        // FriendMapping 엔티티 생성 및 저장 (나 -> 친구)
        FriendMapping friendMapping = new FriendMapping();
        friendMapping.setUser(requestUser);
        friendMapping.setFriendId(recipientUser.getId());
        friendMapping.setStatus(false); // 초기 상태는 false (요청중 상태)
        friendMapping = friendRepository.save(friendMapping);

        //친구 요청 성공 시 반환 DTO
        return FriendConverter.requestFrinedResponse(
                requestUser.getId(),
                recipientUser.getId(),
                friendMapping.getId()
        );
    }

    // 친구 수락
    public FriendResponse.AcceptFriendResponse acceptFriend(Long friendMappingId) {

        FriendMapping friendMapping = friendRepository.findById(friendMappingId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        User requestUser = friendMapping.getUser();

        User recipientUser = userRepository.findById(friendMapping.getFriendId())
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        if(friendRepository.existsByUserIdAndFriendIdAndStatus(requestUser.getId(), recipientUser.getId(),true)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 친구가 맺어진 상대입니다.");
        }

        // 친구 상태 true로 변환(친구 상태)
        friendMapping.setStatus(true);
        friendRepository.save(friendMapping);

        // 역순으로 테이블에 하나 더 저장
        FriendMapping friendMappingReverse = new FriendMapping();
        friendMappingReverse.setUser(recipientUser);
        friendMappingReverse.setFriendId(requestUser.getId());
        friendMappingReverse.setStatus(true);
        friendRepository.save(friendMappingReverse);

        return FriendConverter.acceptFriendResponse(recipientUser.getId(), requestUser.getId(), friendMappingReverse.getFriendId());
    }

    // 친구 요청 거절
    public void rejectFriend(Long friendMappingId) {

        FriendMapping obsoleteFriendMapping = friendRepository.findById(friendMappingId)
                        .orElseThrow(()->new EntityNotFoundException("해당하는 엔티티가 없습니다."));

        if(!obsoleteFriendMapping.getStatus()) {
            friendRepository.delete(obsoleteFriendMapping);
        }

        else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Friend Status에 문제가 있습니다.");
        }
    }

    // 친구 삭제
    public void deleteFriend(Long myId, Long friendId) {
        User myUser = userRepository.findById(myId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 친구 유저가 없습니다."));

        FriendMapping friendMapping1 = friendRepository.findByUserIdAndFriendIdAndStatus(myId,friendId,true);
        FriendMapping friendMapping2 = friendRepository.findByUserIdAndFriendIdAndStatus(friendId,myId,true);

        if(friendMapping1==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구 관계가 없습니다.");
        }

        if(friendMapping2==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 친구 관계가 없습니다.");
        }

        friendRepository.delete(friendMapping1);
        friendRepository.delete(friendMapping2);

    }
    // 친구 목표 보기
    public FriendResponse.FriendObjective readFriendObjective(Long friendId) {

        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 친구 유저가 존재하지 않습니다."));

        List<Objective> sortedObjectives = friendUser.getObjectiveList().stream()
                .sorted(Comparator.comparing(Objective::getStart_day).reversed())
                .toList();

        Optional<Objective> latestObjective = sortedObjectives.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(sortedObjectives.get(0));

        return FriendConverter.friendObjective(latestObjective,friendUser);
    }

    // 친구 운동 달성 기록 보기

    // 친구 체성분 변화 보기(일별)

    // 친구 체성분 변화 보기(월별)

}