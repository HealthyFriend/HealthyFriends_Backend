package server.healthyFriends.domain.entity;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="FoodImage")
@Table(name="food_image")
public class FoodImage extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Lob
    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_record_id",referencedColumnName = "id")
    private FoodRecord foodRecord;
}
