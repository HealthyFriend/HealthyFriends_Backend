package server.healthyFriends.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import server.healthyFriends.service.ExerciseService;

@Tag(name="ExerciseController",description = "기능 구분 : 운동")
@RestController
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
}
