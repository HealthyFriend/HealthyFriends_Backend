package server.healthyFriends.domain;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Objective")
@Table(name="objective")
public class Objective extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private LocalDate start_day;

    private LocalDate end_day;

    @Column(length = 20)
    private String head;

    @Lob
    private String body;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

}
