/*
package server.healthyFriends.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.healthyFriends.repository.FriendRepository;
import server.healthyFriends.repository.ObjectiveRepository;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.service.*;

import jakarta.persistence.EntityManager;
import server.healthyFriends.service.friendmapping.FriendService;
import server.healthyFriends.service.friendmapping.FriendServiceImpl;
import server.healthyFriends.service.objective.ObjectiveSerivce;
import server.healthyFriends.service.objective.ObjectiveServiceImpl;

@Configuration
public class AppConfig {
    //스프링 설정 파일

    @Bean
    public ObjectiveSerivce objectiveSerivce(ObjectiveRepository objectiveRepository, UserRepository userRepository) {
        return new ObjectiveServiceImpl(objectiveRepository,userRepository);
    }

    @Bean
    public FriendService friendService(FriendRepository friendRepository, UserRepository userRepository) {
        return new FriendServiceImpl(friendRepository, userRepository);
    }

}
*/