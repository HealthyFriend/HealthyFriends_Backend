package server.healthyFriends.domain.Entity;

import lombok.*;
import server.healthyFriends.domain.Entity.ExerciseRecord;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity(name="ExerciseSet")
@Table(name="exercise_set")
public class ExerciseSet extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private LocalDate date;

    @Column(length = 3)
    private String exercise_category;

    @Column(length = 15)
    private String exercise_name;

    private Integer set_number;

    private Integer repetitions;

    @Column(precision = 4,scale = 1)
    private BigDecimal weight;

    private Boolean status;

    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_record_id",referencedColumnName = "id")
    private ExerciseRecord exerciseRecord;

}
