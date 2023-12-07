package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

        BodyInfoResponse.CreateBodyInfoResponse createBodyInfoResponse = bodyInfoService.createBodyInfoResponse(userId,createBodyInfoRequest);

        return ResponseUtil.success("체성분 입력 성공",createBodyInfoResponse);
    }
}
