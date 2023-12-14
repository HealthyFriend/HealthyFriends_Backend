package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findById(Long id);

    @Query("SELECT br.weight FROM BodycompositionRecord br " +
    "WHERE br.user.id =:userId " +
    "ORDER BY br.date DESC " +
    "LIMIT 1")
    Optional<BigDecimal> findLatestWeight(@Param("userId") Long userId);
}
