package server.healthyFriends.service;

import server.healthyFriends.web.dto.response.objective.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.web.dto.request.objective.ObjectiveRequest;

import java.util.List;
import java.util.Optional;

public interface ObjectiveSerivce {
    Objective createObjective(Long userId, ObjectiveRequest objectiveRequest);
    ObjectiveResponse readObjective(Long objectiveId);
    Optional<List<ObjectiveResponse>> readObjectives(Long userId);
    Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest);
    void deleteObjective(Long objectiveId);

    Objective findById(Long objectviveId);
}
