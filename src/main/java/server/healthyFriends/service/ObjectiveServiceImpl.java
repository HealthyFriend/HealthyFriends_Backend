package server.healthyFriends.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.ObjectiveConverter;
import server.healthyFriends.web.dto.response.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.ObjectiveRequest;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static server.healthyFriends.converter.ObjectiveConverter.SingleObject;

@Service
@Transactional
@RequiredArgsConstructor
public class ObjectiveServiceImpl implements ObjectiveSerivce{

    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;

    // 목표 생성
    public ObjectiveResponse.CreateObjectiveResponse createObjective(Long userId, ObjectiveRequest.CreateObjectiveRequest createObjectiveRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("해당하는 유저가 없습니다."));

        Objective objective = Objective.builder()
                .start_day(createObjectiveRequest.getStart_day())
                .end_day(createObjectiveRequest.getEnd_day())
                .head(createObjectiveRequest.getHead())
                .body(createObjectiveRequest.getBody())
                .status(false)
                .user(user)
                .build();

        // 목표 저장
        objectiveRepository.save(objective);

        return ObjectiveConverter.createObjectiveResponse(objective.getId());
    }

    // 목표 조회
    public ObjectiveResponse.SingleObjectiveResponse readObjective(Long objectiveId) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

        // 엔터티를 DTO로 매핑
        ObjectiveResponse.SingleObjectiveResponse singleObjectiveResponse = SingleObject(existingObjective);

        return singleObjectiveResponse;

    }

    // 목표 리스트 조회
    public Page<Objective> readObjectives(Long userId, Integer page) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다."));

        Page<Objective> ObjectPage = objectiveRepository.findObjectviesByUser(user, PageRequest.of(page, 10));
        return ObjectPage;
    }

    // 목표 수정
    public Objective updateObjective(Long objectiveId, ObjectiveRequest.UpdateObjectiveRequest updateObjectiveRequest) {

        Objective existingObjective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

        if(updateObjectiveRequest.getStart_day()!=null) {
            existingObjective.setStart_day(updateObjectiveRequest.getStart_day());
        }
        if(updateObjectiveRequest.getEnd_day()!=null) {
            existingObjective.setEnd_day(updateObjectiveRequest.getEnd_day());
        }
        if(updateObjectiveRequest.getHead()!=null) {
            existingObjective.setHead(updateObjectiveRequest.getHead());
        }
        if(updateObjectiveRequest.getBody()!=null) {
            existingObjective.setBody(updateObjectiveRequest.getBody());
        }

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

    public Objective findById(Long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 목표가 없습니다."));

        return objective;
    }
}
