package server.healthyFriends.domain.entity;

import lombok.*;
import server.healthyFriends.domain.entity.DayRecord;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="ExerciseRecord")
@Table(name="exercise_record")
public class ExerciseRecord extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private LocalDate date;

    @Column(length = 3)
    private String exercise_category;

    @Column(length = 15)
    private String exercise_name;

    //최대 중량인 세트의 반복 횟수
    private Integer max_weight_rep;

    @Column(precision = 4,scale = 1)
    private BigDecimal max_weight;

    private LocalTime time;

    @Column(precision = 6, scale = 1)
    private BigDecimal total_exercise_weight;

    private Integer total_set_num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_record_id",referencedColumnName = "id")
    private DayRecord dayRecord;

    @OneToMany(mappedBy = "exerciseRecord",cascade = CascadeType.ALL)
    private List<ExerciseSet> exerciseSetList = new ArrayList<>();
}

