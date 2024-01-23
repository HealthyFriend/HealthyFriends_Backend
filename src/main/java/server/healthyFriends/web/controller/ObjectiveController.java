package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.converter.ObjectiveConverter;
import server.healthyFriends.web.dto.response.ObjectiveResponse;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.web.dto.request.ObjectiveRequest;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.objective.ObjectiveSerivce;
import server.healthyFriends.apiPayload.ResponseUtil;

@Tag(name="ObjectiveController",description = "기능 구분 : 목표")
@RestController
@RequiredArgsConstructor
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveSerivce objectiveSerivce;

    // 목표 설정
    @Operation(summary = "목표 설정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 설정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/setting")
    public ResponseDTO<ObjectiveResponse.CreateObjectiveResponse> createObjective(
            Authentication authentication,
            @RequestBody @Valid ObjectiveRequest.CreateObjectiveRequest createObjectiveRequest) {

            Long userId=Long.parseLong(authentication.getName());

            ObjectiveResponse.CreateObjectiveResponse createObjectiveResponse = objectiveSerivce.createObjective(userId, createObjectiveRequest);

            return ResponseUtil.created("목표 설정 성공",createObjectiveResponse);

    }

    // 목표 조회
    @Operation(summary = "단일 목표 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{objectiveId}")
    public ResponseDTO<ObjectiveResponse.SingleObjectiveResponse> readObjective(
            @PathVariable("objectiveId") Long objectiveId) {

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            ObjectiveResponse.SingleObjectiveResponse singleObjectiveResponse = objectiveSerivce.readObjective(objectiveId);

            return ResponseUtil.success("목표 조회 성공", singleObjectiveResponse);

    }

    // 본인 목표 리스트 조회
    @Operation(summary = "나의 목표 리스트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 리스트 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{userId}/list")
    public ResponseDTO<ObjectiveResponse.ListObjectiveResponse> readMyObjectives(
            Authentication authentication,
            @RequestParam(name="page") Integer page){

        Long userId=Long.parseLong(authentication.getName());

        Page<Objective> objectivePage = objectiveSerivce.readObjectives(userId, page);
        ObjectiveResponse.ListObjectiveResponse listObjectiveResponse = ObjectiveConverter.ListObject(objectivePage);
        return ResponseUtil.success("목표 리스트 조회 성공",listObjectiveResponse);
    }

    // 친구 목표 리스트 조회
    @Operation(summary = "특정 회원의 목표 리스트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 리스트 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/{friendId}/list")
    public ResponseDTO<ObjectiveResponse.ListObjectiveResponse> readFriendObjectives(
            @PathVariable("friendId") Long userId,
            @RequestParam(name="page") Integer page){

        Page<Objective> objectivePage = objectiveSerivce.readObjectives(userId, page);
        ObjectiveResponse.ListObjectiveResponse listObjectiveResponse = ObjectiveConverter.ListObject(objectivePage);
        return ResponseUtil.success("목표 리스트 조회 성공",listObjectiveResponse);
    }

    // 목표 수정
    @Operation(summary = "목표 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/{objectiveId}")
    public ResponseDTO<String> updateObjective(
            @PathVariable("objectiveId") Long objectiveId,
            @RequestBody @Valid ObjectiveRequest.UpdateObjectiveRequest updateObjectiveRequest) {

            Objective objective = objectiveSerivce.findById(objectiveId);

            objectiveSerivce.updateObjective(objectiveId, updateObjectiveRequest);

            return ResponseUtil.success("목표 수정 성공", null);

    }

    // 목표 삭제
    @Operation(summary = "목표 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @DeleteMapping("/{objectiveId}")
    public ResponseDTO<String> deleteObjective(@PathVariable("objectiveId")Long objectiveId) {

            if(objectiveSerivce.findById(objectiveId)==null) {
                return ResponseUtil.notFound("해당하는 목표가 없습니다.",null);
            }

            objectiveSerivce.deleteObjective(objectiveId);

            return ResponseUtil.success("목표 삭제 성공",null);

    }

}
