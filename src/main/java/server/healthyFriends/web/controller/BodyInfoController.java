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
import server.healthyFriends.service.BodyInfoService;
import server.healthyFriends.web.dto.request.BodyInfoRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;

@Tag(name="ExerciseController",description = "기능 구분 : 체성분")
@RestController
@RequiredArgsConstructor
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
}
