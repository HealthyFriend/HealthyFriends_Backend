package server.healthyFriends.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.ObjectiveSerivce;
import server.healthyFriends.service.ObjectiveServiceImpl;
import server.healthyFriends.service.UserService;
import server.healthyFriends.service.UserServiceImpl;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {
    //스프링 설정 파일

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용하여 비밀번호를 안전하게 저장
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Bean
    public ObjectiveSerivce objectiveSerivce(ObjectiveRepository objectiveRepository, UserService userService) {
        return new ObjectiveServiceImpl(objectiveRepository,userService);
    }

}
