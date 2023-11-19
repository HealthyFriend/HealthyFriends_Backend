package server.healthyFriends.domain;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Exercise")
@Table(name="exercise")
public class Exercise extends BaseEntity {
    @GeneratedValue
    @Id
    private long id;

    @Column(nullable = false, length = 3)
    private String exercise_category;

    @Column(nullable = false, length = 15)
    private String exercise_name;

    private Boolean weight;

    private Boolean cardio;

}
