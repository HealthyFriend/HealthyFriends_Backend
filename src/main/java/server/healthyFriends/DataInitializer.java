/*
package server.healthyFriends;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import server.healthyFriends.domain.entity.BodycompositionRecord;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.enums.Gender;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.repository.BodyInfoRepository;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.bodyCompositionRecord.BodyInfoService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;
    private final PasswordEncoder encoder;
    private final BodyInfoRepository bodyInfoRepository;


    @Override
    public void run(String... args) {
        User testUser = User.builder()
                .name("윤덕우")
                .nickname("더구더구")
                .height(new BigDecimal("178.3"))
                .age(27)
                .loginId("더구더구@example.com")
                .password(encoder.encode("password123"))
                .gender(Gender.MALE)
                .role(Role.USER)
                .build();

        User testUser2 = User.builder()
                .name("송하은")
                .nickname("하은콩")
                .height(new BigDecimal("161"))
                .age(24)
                .loginId("하은@example.com")
                .password(encoder.encode("password1234"))
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .build();

        Objective testObjective = Objective.builder()
                .start_day(LocalDate.now())
                .end_day(LocalDate.of(2025,01,21))
                .head("목표 제목")
                .body("목표 내용")
                .status(false)
                .user(testUser)
                .build();

        BodycompositionRecord testBodycompositionRecord = BodycompositionRecord.builder()
                        .body_fat_mass(BigDecimal.valueOf(30.0))
                        .skeletal_muscle_mass(BigDecimal.valueOf(40.0))
                        .weight(BigDecimal.valueOf(90.0))
                        .user(testUser2)
                        .date(LocalDate.of(2023,11,20))
                        .build();

        userRepository.save(testUser);
        userRepository.save(testUser2);
        objectiveRepository.save(testObjective);
        bodyInfoRepository.save(testBodycompositionRecord);
    }
}
*/