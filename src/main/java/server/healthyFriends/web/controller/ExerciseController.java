package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.domain.entity.DayRecord;
import server.healthyFriends.service.exercise.ExerciseService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.web.dto.request.ExerciseRequest;
import server.healthyFriends.web.dto.response.ExerciseResponse;

@Tag(name="ExerciseController",description = "기능 구분 : 운동")
@RestController
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;

    @Operation(summary = "운동 추가")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 추가 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/exercises/exercise-add")
    public ResponseDTO<ExerciseResponse.addExerciseResponse> addExercises(@RequestBody @Valid ExerciseRequest.addExerciseRequest request
            , Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("운동 추가 성공",exerciseService.addExercise(userId,request));
    }

    @Operation(summary = "부위별 운동 조회 (하체(1),가슴(2),등(3),어깨(4),팔(5),복근(6))")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 부위별 운동 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/exercises/category")
    public ResponseDTO<ExerciseResponse.getExerciseResponse> getExercises(@RequestParam("exerciseCoded") Long exerciseCode, Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("부위별 운동 조회 성공",exerciseService.getExerciseResponse(userId,exerciseCode));
    }

    @Operation(summary = "운동 기록 저장")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 기록 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/exercises/record")
    public ResponseDTO<ExerciseResponse.addExerciseDayRecordResponse> addExerciseDayRecord(@RequestBody @Valid ExerciseRequest.exerciseRecordRequest request,
                                                                                     Authentication authentication) {
        Long userId=Long.parseLong(authentication.getName());

        return ResponseUtil.success("운동 기록 등록 성공",exerciseService.addExerciseDayRecord(userId,request));
    }

    @Operation(summary = "운동 기록 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 기록 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/exercises-records/{dayRecordId}")
    public ResponseDTO<ExerciseResponse.getExerciseDayRecordResponse> updateExerciseDayRecord(@RequestBody @Valid ExerciseRequest.exerciseRecordRequest request
            , @PathVariable("dayRecordId") Long dayRecordId) {

        return ResponseUtil.success("운동 기록 수정 성공",exerciseService.updateExerciseDayRecord(dayRecordId, request));
    }

    @Operation(summary = "운동 기록 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 기록 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @DeleteMapping("/exercises-records/{dayRecordId}")
    public ResponseDTO<String> deleteExerciseDayRecord(@PathVariable("dayRecordId") Long dayRecordId) {
        exerciseService.deleteExerciseDayRecord(dayRecordId);

        return ResponseUtil.success("운동 기록 삭제 성공","success");
    }

    @Operation(summary = "운동 기록 상세 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 기록 상세 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/exercise-records/concrete/{dayRecordId}")
    public ResponseDTO<ExerciseResponse.getExerciseDayRecordResponse> getConcreteExerciseDayRecord(@PathVariable("dayRecordId") Long dayRecordId) {

        return ResponseUtil.success("운동 기록 상세 조회 성공",exerciseService.getConcreteExerciseDayRecord(dayRecordId));
    }

    @Operation(summary = "운동 기록 요약 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 운동 기록 요약 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/exercise-records/summary/{dayRecordId}")
    public ResponseDTO<ExerciseResponse.getExerciseDayRecordSummaryResponse> getSummaryExerciseDayRecord(@PathVariable("dayRecordId") Long dayRecordId) {

        return ResponseUtil.success("운동 기록 요약 조회 성공",exerciseService.getSummaryExerciseDayRecord(dayRecordId));
    }
}
