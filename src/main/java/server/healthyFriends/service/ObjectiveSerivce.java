package server.healthyFriends.service;

import org.springframework.data.jpa.repository.JpaRepository;
import server.healthyFriends.domain.Objective;
import server.healthyFriends.domain.dto.ObjectiveRequest;

public interface ObjectiveSerivce {
    public Objective createObjective(Long userId, ObjectiveRequest objectiveRequest);

    public Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest);
}
