package server.healthyFriends;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.enums.Gender;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;
    private final PasswordEncoder encoder;


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

        Objective testObjective = Objective.builder()
                .start_day(LocalDate.now())
                .end_day(LocalDate.of(2025,01,21))
                .head("목표 제목")
                .body("목표 내용")
                .status(false)
                .user(testUser)
                .build();

        userRepository.save(testUser);
        objectiveRepository.save(testObjective);
    }
}
