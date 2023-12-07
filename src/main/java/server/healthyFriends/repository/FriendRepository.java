package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.entity.mapping.FriendMapping;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<FriendMapping, Long> {

    boolean existsByUserIdAndFriendIdAndStatus(Long requestUserId, Long recipientUserId, boolean status);

    FriendMapping findByUserIdAndFriendIdAndStatus(Long requestUserId, Long recipientUserId, boolean status);

    List<FriendMapping> findAllByUserIdAndStatus(Long userId,boolean status);
}
