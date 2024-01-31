package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.service.exercise.ExerciseService;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

@Tag(name="ExerciseController",description = "기능 구분 : 운동")
@RestController
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // 목표 설정
    @Operation(summary = "운동 추가")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 추가 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/exercises/exercise-add")
    public ResponseDTO<ExerciseResponse.addExerciseResponse> addExercise(@RequestBody @Valid ExerciseRequest.addExerciseRequest request
            , Authentication authentication)
    {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("운동 추가 성공",exerciseService.addExercise(userId,request));
    }
}
