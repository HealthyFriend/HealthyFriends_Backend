package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.service.bodyCompositionRecord.BodyInfoService;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

import java.util.Optional;

@Tag(name="BodyInfoController",description = "기능 구분 : 체성분")
@RestController
@RequiredArgsConstructor
@RequestMapping("/body-info")
public class BodyInfoController {

    private final BodyInfoService bodyInfoService;

    @Operation(summary = "체성분 입력")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 설정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/{userId}")
    public ResponseDTO<BodyInfoResponse.CreateBodyInfoResponse> createBodyInfo(
            @RequestBody @Valid  BodyInfoRequest.CreateBodyInfoRequest createBodyInfoRequest,
            @PathVariable("userId") Long userId
            ) {

        BodyInfoResponse.CreateBodyInfoResponse createBodyInfoResponse = bodyInfoService.createBodyInfo(userId,createBodyInfoRequest);

        return ResponseUtil.success("체성분 입력 성공",createBodyInfoResponse);
    }

    @Operation(summary = "체성분 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 목표 설정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/{bodyCompositionRecordId}")
    public ResponseDTO<String> updateBodyInfo(
            @RequestBody @Valid  BodyInfoRequest.UpdateBodyInfoRequest updateBodyInfoRequest,
            @PathVariable("bodyCompositionRecordId") Long bodyCompositionRecordId
    ) {

        bodyInfoService.updateBodyInfo(bodyCompositionRecordId, updateBodyInfoRequest);

        return ResponseUtil.success("체성분 수정 성공",null);
    }

    @Operation(summary = "몸무게 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 일별 몸무게 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/users/{userId}/weight/daily")
    public ResponseDTO<BodyInfoResponse.DailyWeightChange> dailyMuscleChange(@PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyWeightChange> dailyWeightChangeOptional = bodyInfoService.getDailyWeightChange(userId);

        BodyInfoResponse.DailyWeightChange dailyWeightChange = dailyWeightChangeOptional.orElse(null);

        if (dailyWeightChange != null) {
            return ResponseUtil.success("일별 몸무게 변화 기록 조회 성공", dailyWeightChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 몸무게 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "골격근 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 일별 골격근 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/users/{userId}/muscle/daily")
    public ResponseDTO<BodyInfoResponse.DailyMuscleChange> dailyWeightChange(@PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyMuscleChange> dailyMuscleChangeOptional = bodyInfoService.getDailyMuscleChange(userId);

        BodyInfoResponse.DailyMuscleChange dailyMuscleChange = dailyMuscleChangeOptional.orElse(null);

        if (dailyMuscleChange != null) {
            return ResponseUtil.success("일별 골격근 변화 기록 조회 성공", dailyMuscleChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 골격근 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "체지방 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 일별 체지방 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/users/{userId}/fat/daily")
    public ResponseDTO<BodyInfoResponse.DailyFatChange> dailyFatChange(@PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyFatChange> dailyFatChangeOptional = bodyInfoService.getDailyFatChange(userId);

        BodyInfoResponse.DailyFatChange dailyFatChange = dailyFatChangeOptional.orElse(null);

        if (dailyFatChange != null) {
            return ResponseUtil.success("일별 체지방 변화 기록 조회 성공", dailyFatChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 체지방 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "bmi 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 bmi 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/users/{userId}/bmi/daily")
    public ResponseDTO<BodyInfoResponse.DailyBmiChange> dailyBmiChange(@PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyBmiChange> dailyBmiChangeOptional = bodyInfoService.getDailyBmiChange(userId);

        BodyInfoResponse.DailyBmiChange dailyBmiChange = dailyBmiChangeOptional.orElse(null);

        if (dailyBmiChange != null) {
            return ResponseUtil.success("일별 bmi 변화 기록 조회 성공", dailyBmiChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 bmi 기록이 없습니다.", null);
        }
    }
}
