package server.healthyFriends.service;

import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

public interface BodyInfoService {

    BodyInfoResponse.CreateBodyInfoResponse createBodyInfo(Long userId, BodyInfoRequest.CreateBodyInfoRequest req);
    void updateBodyInfo(Long bodyCompositionRecordId, BodyInfoRequest.UpdateBodyInfoRequest req);
}
