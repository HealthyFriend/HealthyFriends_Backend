package server.healthyFriends.service;

import org.springframework.data.domain.Page;
import server.healthyFriends.web.dto.response.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.web.dto.request.ObjectiveRequest;

import java.util.List;
import java.util.Optional;

public interface ObjectiveSerivce {
    ObjectiveResponse.CreateObjectiveResponse createObjective(Long userId, ObjectiveRequest.CreateObjectiveRequest createObjectiveRequest);
    ObjectiveResponse.SingleObjectiveResponse readObjective(Long objectiveId);
    Page<Objective> readObjectives(Long userId, Integer page);
    Objective updateObjective(Long objectiveId, ObjectiveRequest.UpdateObjectiveRequest updateObjectiveRequest);
    void deleteObjective(Long objectiveId);
    Objective findById(Long objectiveId);
}
