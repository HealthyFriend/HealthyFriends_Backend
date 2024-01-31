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

    //운동 부위별 코드 ex)하체:1, 가슴:2, 등:3, 어깨:4, 팔:5, 복근:6
    @Column(nullable = false, length = 3)
    private Long exercise_code;

    //웨이트 운동 여부
    @Column(nullable = true)
    private Boolean isWeight;

    //유산소 운동 여부
    @Column(nullable = true)
    private Boolean isCardio;

    @OneToMany(mappedBy = "exercise",cascade = CascadeType.ALL)
    private List<ExerciseMapping> exerciseMappingList = new ArrayList<>();
}
