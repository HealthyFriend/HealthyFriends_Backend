package server.healthyFriends.domain.mapping;

import lombok.*;
import server.healthyFriends.domain.Entity.Exercise;
import server.healthyFriends.domain.Entity.User;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
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
