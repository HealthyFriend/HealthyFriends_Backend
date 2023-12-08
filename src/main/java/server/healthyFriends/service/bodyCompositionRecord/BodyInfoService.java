package server.healthyFriends.service.bodyCompositionRecord;

import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.util.List;
import java.util.Optional;

public interface BodyInfoService {

    BodyInfoResponse.CreateBodyInfoResponse createBodyInfo(Long userId, BodyInfoRequest.CreateBodyInfoRequest req);
    void updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req);
    Optional<BodyInfoResponse.DailyWeightChange> getDailyWeightChange(Long userId, Long friendId);
}
