package server.healthyFriends.service.friendmapping;

import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;
import server.healthyFriends.web.dto.response.FriendResponse;

import java.util.List;
import java.util.Optional;

public interface FriendService {

    FriendResponse.FindFriendResponse findFriendbyLoginId(String friendLoginId);
    FriendResponse.FindFriendResponse findFriendbyNickname(String friendNickname);
    FriendResponse.RequestFriendResponse requestFriend(Long userId, FriendRequest.RequestFriendRequest requestFriendRequest);
    FriendResponse.AcceptFriendResponse acceptFriend(Long friendMappingId);
    Optional<List<FriendResponse.MappingFriendResponse>> mappingFriend(Long userId);
    Optional<List<FriendResponse.MappingFriendResponse>> prospectiveFriend(Long userId,Boolean isFriend);
    Optional<BodyInfoResponse.DailyWeightChange> getDailyFriendWeightChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyMuscleChange> getDailyFriendMuscleChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyFatChange> getDailyFriendFatChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyBmiChange> getDailyFriendBmiChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.MonthlyWeightChange> getFriendMonthlyWeightChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.MonthlyMuscleChange> getFriendMonthlyMuscleChange(Long userId,Long friendId);
    Optional<BodyInfoResponse.MonthlyFatChange> getFriendMonthlyFatChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.MonthlyBmiChange> getFriendMonthlyBmiChange(Long userId, Long friendId);
    void rejectFriend(Long friendMappingId);
    void deleteFriend(Long myId, Long friendId);
    FriendResponse.FriendObjective readFriendObjective(Long friendId);
}
