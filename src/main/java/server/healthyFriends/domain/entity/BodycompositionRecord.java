package server.healthyFriends.domain.entity;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="BodycompositionRecord")
@Table(name="bodycomposition_record")
public class BodycompositionRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(precision = 3, scale = 1)
    private BigDecimal skeletal_muscle_mass;

    @Column(precision = 3, scale = 1)
    private BigDecimal body_fat_mass;

    @Column(precision = 3, scale = 1)
    private BigDecimal bmi;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
