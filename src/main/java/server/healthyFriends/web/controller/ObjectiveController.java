package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.domain.dto.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.dto.ObjectiveRequest;
import server.healthyFriends.domain.dto.ResponseDTO;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.service.ObjectiveSerivce;
import server.healthyFriends.service.UserService;
import server.healthyFriends.web.response.ResponseUtil;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveSerivce objectiveSerivce;
    private final ObjectiveRepository objectiveRepository;
    private final UserService userService;

    // 목표 설정
    @PostMapping("/{userId}")
    public ResponseDTO<String> createObjective(
            @PathVariable("userId") Long userId,
            @RequestBody ObjectiveRequest objectiveRequest) {
        try {
            User user = userService.getUserById(userId);

            if(user==null) {
                return ResponseUtil.notFound("해당하는 사용자가 없습니다.",null);
            }

            objectiveSerivce.createObjective(userId, objectiveRequest);

            return ResponseUtil.created("목표 설정 성공",null);

        } catch (Exception e) {
            throw e;
        }
    }

    // 목표 조회
    @GetMapping("/{objectiveId}")
    public ResponseDTO<ObjectiveResponse> readObjective(
            @PathVariable("objectiveId") Long objectiveId) {

        try {

            if(objectiveRepository.findById(objectiveId).isEmpty()) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            ObjectiveResponse objectiveResponse = objectiveSerivce.readObjective(objectiveId);

            return ResponseUtil.success("목표 조회 성공", objectiveResponse);

        } catch(Exception e){
            throw e;
        }
    }

    // 목표 수정
    @PutMapping("/{objectiveId}")
    public ResponseDTO<String> updateObjective(
            @PathVariable("objectiveId") Long objectiveId,
            @RequestBody ObjectiveRequest objectiveRequest) {
        try {

            Objective objective = objectiveRepository.findById(objectiveId)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

            objectiveSerivce.updateObjective(objectiveId, objectiveRequest);

            return ResponseUtil.success("목표 수정 성공", null);
        } catch(EntityNotFoundException e){
            return ResponseUtil.notFound(e.getMessage(), null);
        }
        catch (Exception e) {
            throw e;
        }
    }

    // 목표 삭제
    @DeleteMapping("/{objectiveId}")
    public ResponseDTO<String> deleteObjective(@PathVariable("objectiveId")Long objectiveId) {

        try {

            if(objectiveRepository.findById(objectiveId).isEmpty()) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            objectiveSerivce.deleteObjective(objectiveId);

            return ResponseUtil.success("목표 삭제 성공",null);

        } catch (Exception e) {
            throw e;
        }
    }

}
