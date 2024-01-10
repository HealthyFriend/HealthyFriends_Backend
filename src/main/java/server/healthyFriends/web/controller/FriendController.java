package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.service.bodyCompositionRecord.BodyInfoService;
import server.healthyFriends.web.dto.request.FriendRequest;
import server.healthyFriends.web.dto.response.BodyInfoResponse;
import server.healthyFriends.web.dto.response.FriendResponse;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.service.user.UserService;
import server.healthyFriends.apiPayload.ResponseUtil;

import java.util.Optional;


@Tag(name="FriendController",description = "기능 구분 : 친구")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final BodyInfoService bodyInfoService;

    @Operation(summary = "아이디로 친구 찾기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendLoginId}")
    public ResponseDTO<FriendResponse.FindFriendResponse> findFriend(@RequestParam(name = "friendLoginId") String friendLoginId) {

        FriendResponse.FindFriendResponse findFriendResponse = friendService.findFriendResponse(friendLoginId);

        return ResponseUtil.success("친구 찾기 성공", findFriendResponse);
    }

    @Operation(summary = "친구 요청 수락")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/friend-acceptance/{friendMappingId}")
    public ResponseDTO<FriendResponse.AcceptFriendResponse> acceptFriend (
            @PathVariable("friendMappingId") Long friendMappingId) {

        FriendResponse.AcceptFriendResponse acceptFriendResponse = friendService.acceptFriend(friendMappingId);

        return ResponseUtil.success("친구 요청을 수락했습니다.",acceptFriendResponse);
    }

    @Operation(summary = "친구 요청 거절")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })

    @DeleteMapping("/friend-rejection/{friendMappingId}")
    public ResponseDTO<String> rejectFriend (
            @PathVariable("friendMappingId") Long friendMappingId) {
        friendService.rejectFriend(friendMappingId);

        return ResponseUtil.success("친구 거절 성공",null);
    }

    @Operation(summary = "친구 목표 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 친구 신청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code", description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/objective")
    public ResponseDTO<FriendResponse.FriendObjective> readFriendObjective(@PathVariable("friendId")Long friendId) {

        FriendResponse.FriendObjective friendObjective = friendService.readFriendObjective(friendId);

        return ResponseUtil.success("친구 목표 조회 성공",friendObjective);
    }

    @Operation(summary = "친구 몸무게 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 몸무게 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/users/{userId}/weight/daily")
    public ResponseDTO<BodyInfoResponse.DailyWeightChange> dailyMuscleChange(@PathVariable("friendId")Long friendId,
                                                                             @PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyWeightChange> dailyWeightChangeOptional = bodyInfoService.getDailyFriendWeightChange(userId, friendId);

        BodyInfoResponse.DailyWeightChange dailyWeightChange = dailyWeightChangeOptional.orElse(null);

        if (dailyWeightChange != null) {
            return ResponseUtil.success("일별 몸무게 변화 기록 조회 성공", dailyWeightChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 몸무게 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "친구 골격근 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 골격근 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/users/{userId}/muscle/daily")
    public ResponseDTO<BodyInfoResponse.DailyMuscleChange> dailyWeightChange(@PathVariable("friendId")Long friendId,
                                                                             @PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyMuscleChange> dailyMuscleChangeOptional = bodyInfoService.getDailyFriendMuscleChange(userId, friendId);

        BodyInfoResponse.DailyMuscleChange dailyMuscleChange = dailyMuscleChangeOptional.orElse(null);

        if (dailyMuscleChange != null) {
            return ResponseUtil.success("친구의 일별 골격근 변화 기록 조회 성공", dailyMuscleChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 골격근 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "친구 체지방 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 체지방 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/users/{userId}/fat/daily")
    public ResponseDTO<BodyInfoResponse.DailyFatChange> dailyFatChange(@PathVariable("friendId")Long friendId,
                                                                             @PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyFatChange> dailyFatChangeOptional = bodyInfoService.getDailyFriendFatChange(userId, friendId);

        BodyInfoResponse.DailyFatChange dailyFatChange = dailyFatChangeOptional.orElse(null);

        if (dailyFatChange != null) {
            return ResponseUtil.success("친구의 일별 체지방 변화 기록 조회 성공", dailyFatChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 체지방 기록이 없습니다.", null);
        }
    }

    @Operation(summary = "친구 bmi 일별 조회(1년간)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 친구의 일별 bmi 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/friends/{friendId}/users/{userId}/bmi/daily")
    public ResponseDTO<BodyInfoResponse.DailyBmiChange> dailyBmiChange(@PathVariable("friendId")Long friendId,
                                                                       @PathVariable("userId")Long userId) {

        Optional<BodyInfoResponse.DailyBmiChange> dailyBmiChangeOptional = bodyInfoService.getDailyFriendBmiChange(userId, friendId);

        BodyInfoResponse.DailyBmiChange dailyBmiChange = dailyBmiChangeOptional.orElse(null);

        if (dailyBmiChange != null) {
            return ResponseUtil.success("친구의 일별 bmi 변화 기록 조회 성공", dailyBmiChange);
        } else {
            return ResponseUtil.success("지난 1년간 입력된 bmi 기록이 없습니다.", null);
        }
    }
}
