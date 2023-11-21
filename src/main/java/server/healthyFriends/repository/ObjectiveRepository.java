package server.healthyFriends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.healthyFriends.domain.Objective;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

}
