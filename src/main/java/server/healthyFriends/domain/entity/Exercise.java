package server.healthyFriends.domain.entity;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;
import server.healthyFriends.domain.entity.mapping.ExerciseMapping;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "exercise",cascade = CascadeType.ALL)
    private List<ExerciseMapping> exerciseMappingList = new ArrayList<>();
}
