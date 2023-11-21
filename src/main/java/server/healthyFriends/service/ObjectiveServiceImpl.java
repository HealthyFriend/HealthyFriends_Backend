package server.healthyFriends.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.dto.ObjectiveRequest;
import server.healthyFriends.repository.ObjectiveRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ObjectiveServiceImpl implements ObjectiveSerivce{

    private final ObjectiveRepository objectiveRepository;
    private final UserService userService;

    // 목표 생성
    public Objective createObjective(Long userId, ObjectiveRequest objectiveRequest) {

        User user = userService.getUserById(userId);

        Objective objective = Objective.builder()
                .start_day(objectiveRequest.getStart_day())
                .end_day(objectiveRequest.getEnd_day())
                .head(objectiveRequest.getHead())
                .body(objectiveRequest.getBody())
                .status(false)
                .user(user)
                .build();

        // 목표 저장
        Objective savedObjective = objectiveRepository.save(objective);

        return savedObjective;
    }

    // 목표 수정
    public Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("해당하는 목표를 찾을 수 없습니다."));

        existingObjective.setStart_day(objectiveRequest.getStart_day());
        existingObjective.setEnd_day(objectiveRequest.getEnd_day());
        existingObjective.setHead(objectiveRequest.getHead());
        existingObjective.setBody(objectiveRequest.getBody());

        // 목표 저장
        Objective updatedObjective = objectiveRepository.save(existingObjective);

        return updatedObjective;

    }
}
