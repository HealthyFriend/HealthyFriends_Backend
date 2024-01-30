package server.healthyFriends.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import server.healthyFriends.converter.FriendConverter;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.UserRepository;

import jakarta.transaction.Transactional;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.web.dto.response.UserResponse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder encoder;

    public User getUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));
        return user;
    }

    public void withdrawal(Long userId, UserRequest.WithdrawalRequest req) {

        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        if(encoder.matches(req.getPassword(), user.getPassword())) {
            userRepository.delete(user);
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호가 올바르지 않습니다.");
        }

    }

    public UserResponse.UserInfoResponse modifyUserInfo(Long userId, UserRequest.ModifyUserInfoRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 회원이 없습니다."));

        if(req.getAge()!=null) {
            user.setAge(req.getAge());
        }

        if(req.getName()!=null) {
            user.setName(req.getName());
        }

        if(req.getNickname()!=null) {
            user.setNickname(req.getNickname());
        }

        if(req.getGender()!=null) {
            user.setGender(req.getGender());
        }

        if(req.getPassword()!=null) {
            user.setPassword(encoder.encode(req.getPassword()));
        }

        if(req.getHeight()!=null) {
            user.setHeight(req.getHeight());
        }

        userRepository.save(user);

        return UserResponse.UserInfoResponse.builder()
                .age(user.getAge())
                .loginId(user.getLoginId())
                .name(user.getName())
                .nickname(user.getNickname())
                .height(user.getHeight())
                .build();
    }

    public Optional<UserResponse.UserInfoResponse> getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));


        //Optional<BigDecimal> weight = userRepository.findLatestWeight(userId);

        UserResponse.UserInfoResponse userInfoResponse = UserResponse.UserInfoResponse.builder()
                .age(user.getAge())
                .loginId(user.getLoginId())
                .name(user.getName())
                .nickname(user.getNickname())
                .height(user.getHeight())
                //.weight(weight)
                .build();

        return Optional.of(userInfoResponse);
    }
    public FriendResponse.ListFriendResponse readFriends(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        List<FriendMapping> friendMappingList = friendRepository.findAllByUserIdAndStatus(userId,true);

        List<FriendResponse.FriendInfo> friendInfoList = friendMappingList.stream()
                .map(friendMapping -> {
                    Long friendId = friendMapping.getFriendId();
                    User friendUser = userRepository.findById(friendId)
                            .orElseThrow(()->new EntityNotFoundException("해당하는 친구 유저가 존재하지 않습니다."));

                    List<Objective> sortedObjectives = friendUser.getObjectiveList().stream()
                            .sorted(Comparator.comparing(Objective::getStart_day).reversed())
                            .toList();

                    String latestObjectiveHead = sortedObjectives.isEmpty() ? null : sortedObjectives.get(0).getHead();

                    return FriendConverter.friendInfo(friendUser.getId(), friendUser.getNickname(), latestObjectiveHead);
                })
                .toList();

        return FriendResponse.ListFriendResponse.builder()
                .friendInfoList(friendInfoList)
                .build();

    }
}

