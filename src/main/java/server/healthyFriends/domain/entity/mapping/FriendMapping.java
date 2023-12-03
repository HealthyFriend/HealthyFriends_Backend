package server.healthyFriends.domain.entity.mapping;

import lombok.*;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.common.BaseEntity;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FriendMapping")
@Table(name="friend_mapping")
public class FriendMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long friendId;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

}
