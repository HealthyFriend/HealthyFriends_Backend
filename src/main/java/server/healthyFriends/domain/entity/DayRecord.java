package server.healthyFriends.domain.entity;

import lombok.*;
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
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity(name="DayRecord")
@Table(name="day_record")
public class DayRecord extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private LocalDate date;

    private LocalTime total_time;

    @Column(precision=7, scale=1)
    private BigDecimal total_weight;

    @Column(precision=4, scale=2)
    private BigDecimal complete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "dayRecord",cascade = CascadeType.ALL)
    private List<ExerciseRecord> exerciseRecordList = new ArrayList<>();
}

