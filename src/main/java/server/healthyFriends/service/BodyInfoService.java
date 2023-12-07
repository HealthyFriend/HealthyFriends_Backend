package server.healthyFriends.service;

import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

public interface BodyInfoService {

    BodyInfoResponse.CreateBodyInfoResponse createBodyInfoResponse(Long userId, BodyInfoRequest.CreateBodyInfoRequest req);
}
