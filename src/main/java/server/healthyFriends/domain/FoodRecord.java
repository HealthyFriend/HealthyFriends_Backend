package server.healthyFriends.domain;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity(name="FoodRecord")
@Table(name="food_record")
public class FoodRecord extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Lob
    private String body;

    private LocalDate date;
}
