package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findById(Long id);
}
