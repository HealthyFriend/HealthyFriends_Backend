package server.healthyFriends.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.UserService;
import server.healthyFriends.service.UserServiceImpl;

@Configuration
public class AppConfig {
    //스프링 설정 파일

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}
