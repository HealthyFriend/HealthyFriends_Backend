package server.healthyFriends.service.bodyCompositionRecord;

import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.util.List;
import java.util.Optional;

public interface BodyInfoService {

    BodyInfoResponse.CreateBodyInfoResponse createBodyInfo(Long userId, BodyInfoRequest.CreateBodyInfoRequest req);
    void updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req);
    Optional<BodyInfoResponse.DailyWeightChange> getDailyFriendWeightChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyMuscleChange> getDailyFriendMuscleChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyFatChange> getDailyFriendFatChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyBmiChange> getDailyFriendBmiChange(Long userId, Long friendId);
    Optional<BodyInfoResponse.DailyWeightChange> getDailyWeightChange(Long userId);
    Optional<BodyInfoResponse.DailyMuscleChange> getDailyMuscleChange(Long userId);
    Optional<BodyInfoResponse.DailyFatChange> getDailyFatChange(Long userId);
    Optional<BodyInfoResponse.DailyBmiChange> getDailyBmiChange(Long userId);
    Optional<BodyInfoResponse.MonthlyWeightChange> getMonthlyWeightChange(Long userId);
    Optional<BodyInfoResponse.MonthlyMuscleChange> getMonthlyMuscleChange(Long userId);
    Optional<BodyInfoResponse.MonthlyFatChange> getMonthlyFatChange(Long userId);
    Optional<BodyInfoResponse.MonthlyBmiChange> getMonthlyBmiChange(Long userId);


}
