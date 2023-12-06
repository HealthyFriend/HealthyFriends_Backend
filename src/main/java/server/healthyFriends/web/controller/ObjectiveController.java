package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.converter.ObjectiveConverter;
import server.healthyFriends.web.dto.response.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.web.dto.request.ObjectiveRequest;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.ObjectiveSerivce;
import server.healthyFriends.service.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Tag(name="ObjectiveController",description = "기능 구분 : 목표")
@RestController
@RequiredArgsConstructor
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveSerivce objectiveSerivce;
    private final UserService userService;

    // 목표 설정
    @Operation(summary = "목표 설정")
    @PostMapping("/{userId}")
    public ResponseDTO<String> createObjective(
            @PathVariable("userId") Long userId,
            @RequestBody ObjectiveRequest objectiveRequest) {

            User user = userService.getUserById(userId);

            if(user==null) {
                return ResponseUtil.notFound("해당하는 사용자가 없습니다.",null);
            }

            objectiveSerivce.createObjective(userId, objectiveRequest);

            return ResponseUtil.created("목표 설정 성공",null);

    }

    // 목표 조회
    @Operation(summary = "단일 목표 조회")
    @GetMapping("/{objectiveId}")
    public ResponseDTO<ObjectiveResponse.SingleObjectiveResponse> readObjective(
            @PathVariable("objectiveId") Long objectiveId) {

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            ObjectiveResponse.SingleObjectiveResponse singleObjectiveResponse = objectiveSerivce.readObjective(objectiveId);

            return ResponseUtil.success("목표 조회 성공", singleObjectiveResponse);

    }

    // 목표 리스트 조회
    @Operation(summary = "특정 회원의 목표 리스트 조회")
    @GetMapping("/{userId}/list")
    public ResponseDTO<ObjectiveResponse.ListObjectiveResponse> readObjectives(
            @PathVariable("userId") Long userId,
            @RequestParam(name="page") Integer page){

        Page<Objective> objectivePage = objectiveSerivce.readObjectives(userId, page);
        ObjectiveResponse.ListObjectiveResponse listObjectiveResponse = ObjectiveConverter.ListObject(objectivePage);
        return ResponseUtil.success("목표 리스트 조회 성공",listObjectiveResponse);
    }

    // 목표 수정
    @Operation(summary = "목표 수정")
    @PutMapping("/{objectiveId}")
    public ResponseDTO<String> updateObjective(
            @PathVariable("objectiveId") Long objectiveId,
            @RequestBody ObjectiveRequest objectiveRequest) {

            Objective objective = objectiveSerivce.findById(objectiveId);

            objectiveSerivce.updateObjective(objectiveId, objectiveRequest);

            return ResponseUtil.success("목표 수정 성공", null);

    }

    // 목표 삭제
    @Operation(summary = "목표 삭제")
    @DeleteMapping("/{objectiveId}")
    public ResponseDTO<String> deleteObjective(@PathVariable("objectiveId")Long objectiveId) {

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            objectiveSerivce.deleteObjective(objectiveId);

            return ResponseUtil.success("목표 삭제 성공",null);

    }

}
