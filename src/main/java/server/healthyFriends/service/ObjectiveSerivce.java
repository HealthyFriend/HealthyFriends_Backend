package server.healthyFriends.service;

import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.dto.ObjectiveRequest;

public interface ObjectiveSerivce {
    public Objective createObjective(Long userId, ObjectiveRequest objectiveRequest);

    public Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest);
}
