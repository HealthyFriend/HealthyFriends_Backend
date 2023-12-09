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

@Tag(name="ExerciseController",description = "기능 구분 : 체성분")
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

    @Operation(summary = "친구 몸무게 일별 조회(3개월간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 몸무게 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/users/{userId}/weight/daily")
    public ResponseDTO<BodyInfoResponse.DailyWeightChange> dailyWeightChange(@PathVariable("friendId")Long friendId,
                                                                             @PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyWeightChange> dailyWeightChangeOptional = bodyInfoService.getDailyWeightChange(userId, friendId);

        BodyInfoResponse.DailyWeightChange dailyWeightChange = dailyWeightChangeOptional.orElse(null);

        if (dailyWeightChange != null) {
            return ResponseUtil.success("일별 몸무게 변화 기록 조회 성공", dailyWeightChange);
        } else {
            return ResponseUtil.success("입력된 몸무게 기록이 없습니다.", null);
        }
    }
}
