package server.healthyFriends.domain.entity;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import server.healthyFriends.domain.common.BaseEntity;
import server.healthyFriends.domain.entity.mapping.ExerciseMapping;
import server.healthyFriends.domain.entity.mapping.FriendMapping;
import server.healthyFriends.domain.enums.Gender;
import server.healthyFriends.domain.enums.Role;

import jakarta.persistence.*;
import server.healthyFriends.sercurity.OAuth.Basic.OAuthProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
//생성자 접근 수준 protected
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="User")
@Table(name="user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String name;

    @Column(nullable = true, length = 15)
    private String nickname;

    @Column(nullable = true, precision = 4, scale = 1)
    private BigDecimal height;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = false, length = 40, unique = true)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Objective> objectiveList = new ArrayList<>();

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
    private List<FoodRecord> foodRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ExerciseMapping> exerciseMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<DayRecord> dayRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<BodycompositionRecord> bodycompositionRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<FriendMapping> friendMappingList = new ArrayList<>();
}
