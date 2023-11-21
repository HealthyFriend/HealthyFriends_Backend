package server.healthyFriends;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import server.healthyFriends.domain.User;
import server.healthyFriends.domain.enums.Gender;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.repository.UserRepository;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        User user = User.builder()
                .name("윤덕우")
                .nickname("더구더구")
                .height(new BigDecimal("178.3"))
                .age(27)
                .loginId("더구더구@example.com")
                .password(encoder.encode("password123"))
                .gender(Gender.MALE)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
