package server.healthyFriends.domain.entity.mapping;

import lombok.*;
import server.healthyFriends.domain.entity.Exercise;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ExerciseMapping")
@Table(name="exercise_mapping")
public class ExerciseMapping extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_id",referencedColumnName = "id")
    private Exercise exercise;
}
