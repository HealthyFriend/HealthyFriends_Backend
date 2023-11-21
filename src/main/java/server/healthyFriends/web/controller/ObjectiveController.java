package server.healthyFriends.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.healthyFriends.domain.Entity.Objective;
import server.healthyFriends.domain.Entity.User;
import server.healthyFriends.domain.dto.ObjectiveRequest;
import server.healthyFriends.domain.dto.ResponseDTO;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.service.ObjectiveSerivce;
import server.healthyFriends.service.UserService;
import server.healthyFriends.web.response.ResponseUtil;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveSerivce objectiveSerivce;
    private final ObjectiveRepository objectiveRepository;
    private final UserService userService;

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

            return ResponseUtil.success("목표 설정 성공",null);

        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{objectiveId}")
    public ResponseDTO<String> updateObjective(
            @PathVariable("objectiveId") Long objectiveId,
            @RequestBody ObjectiveRequest objectiveRequest) {
        try {

            Objective objective = objectiveRepository.findById(objectiveId)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 목표를 찾을 수 없습니다."));

            objectiveSerivce.updateObjective(objectiveId, objectiveRequest);

            return ResponseUtil.success("목표 수정 성공", null);
        } catch(EntityNotFoundException e){
            return ResponseUtil.notFound(e.getMessage(), null);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
