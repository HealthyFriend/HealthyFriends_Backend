package server.healthyFriends.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.domain.dto.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.dto.ObjectiveRequest;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.web.response.EntityDtoMapper;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 목표 조회
    public ObjectiveResponse readObjective(Long objectiveId) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

        // 엔터티를 DTO로 매핑
        ObjectiveResponse objectiveResponse = EntityDtoMapper.INSTANCE.objectivetoDto(existingObjective);

        return objectiveResponse;

    }

    // 목표 리스트 조회
    public Optional<List<ObjectiveResponse>> readObjectives(Long userId) {

        Optional<List<Objective>> objectivesList = objectiveRepository.findByUserId(userId);

        // objectivesOptional가 비어있으면 빈 Optional 반환
        return objectivesList.map(objectives -> {
            List<ObjectiveResponse> objectiveResponses = objectives.stream()
                    .map(EntityDtoMapper.INSTANCE::objectivetoDto)
                    .collect(Collectors.toList());
            return Optional.of(objectiveResponses);
        }).orElse(Optional.empty());

        /*
            if (objectivesList.isEmpty()) {
                return Optional.empty();
            }

             List<ObjectiveResponse> objectiveResponses = new ArrayList<>();
             for (Objective objective : objectivesList.get()) {
                objectiveResponses.add(new ObjectiveResponse(objective.getStartDay(), objective.getEndDay(),
                       objective.getHead(), objective.getBody(), objective.getStatus()));
             }
        */
    }

    // 목표 수정
    public Objective updateObjective(Long objectiveId, ObjectiveRequest objectiveRequest) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

        existingObjective.setStart_day(objectiveRequest.getStart_day());
        existingObjective.setEnd_day(objectiveRequest.getEnd_day());
        existingObjective.setHead(objectiveRequest.getHead());
        existingObjective.setBody(objectiveRequest.getBody());

        // 목표 저장
        Objective updatedObjective = objectiveRepository.save(existingObjective);

        return updatedObjective;

    }

    // 목표 삭제
    public void deleteObjective(Long objectiveId) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

        objectiveRepository.delete(existingObjective);
    }
}
