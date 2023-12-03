package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.web.dto.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.ObjectiveRequest;
import server.healthyFriends.web.dto.ResponseDTO;
import server.healthyFriends.service.ObjectiveSerivce;
import server.healthyFriends.service.UserService;
import server.healthyFriends.web.response.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveSerivce objectiveSerivce;
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

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            ObjectiveResponse objectiveResponse = objectiveSerivce.readObjective(objectiveId);

            return ResponseUtil.success("목표 조회 성공", objectiveResponse);

        } catch(Exception e){
            throw e;
        }
    }

    // 목표 리스트 조회
    @GetMapping("/{userId}/list")
    public ResponseDTO<Optional<List<ObjectiveResponse>>> readObjectives(
            @PathVariable("userId") Long userId) {

        try {
            User user = userService.getUserById(userId);

            if(user==null) {
                return ResponseUtil.notFound("해당하는 사용자가 없습니다.",null);
            }

            return ResponseUtil.success("목표 리스트 조회 성공",objectiveSerivce.readObjectives(userId));

        } catch (Exception e) {
            throw e;
        }
    }

    // 목표 수정
    @PutMapping("/{objectiveId}")
    public ResponseDTO<String> updateObjective(
            @PathVariable("objectiveId") Long objectiveId,
            @RequestBody ObjectiveRequest objectiveRequest) {
        try {

            Objective objective = objectiveSerivce.findById(objectiveId);

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

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            objectiveSerivce.deleteObjective(objectiveId);

            return ResponseUtil.success("목표 삭제 성공",null);

        } catch (Exception e) {
            throw e;
        }
    }

}
