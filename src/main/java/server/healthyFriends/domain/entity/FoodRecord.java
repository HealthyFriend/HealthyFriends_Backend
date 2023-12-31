package server.healthyFriends.domain.entity;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "foodRecord",cascade = CascadeType.ALL)
    private List<FoodImage> foodImageList = new ArrayList<>();
}
