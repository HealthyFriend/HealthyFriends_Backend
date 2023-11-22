package server.healthyFriends.service;

import server.healthyFriends.domain.dto.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.dto.ObjectiveRequest;

import java.util.List;
import java.util.Optional;

public interface ObjectiveSerivce {
    public Objective createObjective(Long userId, ObjectiveRequest objectiveRequest);
    public ObjectiveResponse readObjective(Long objectiveId);
    public Optional<List<ObjectiveResponse>> readObjectives(Long userId);
    public Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest);
    public void deleteObjective(Long objectiveId);
}
