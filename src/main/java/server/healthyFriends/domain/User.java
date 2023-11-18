package server.healthyFriends.domain;

import lombok.*;
import server.healthyFriends.domain.common.BaseEntity;
import server.healthyFriends.domain.enums.Gender;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
//생성자 접근 수준 protected
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="User")
@Table(name="user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 4)
    private String name;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal height;

    private Integer age;

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
