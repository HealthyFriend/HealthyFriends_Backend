package server.healthyFriends.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.enums.Gender;
import server.healthyFriends.domain.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message="아이디를 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "이름을 입력하세요." )
    private String name;

    @NotBlank(message = "닉네임을 입력하세요." )
    private String nickname;

    @NotNull(message = "키를 입력하세요." )
    private BigDecimal height;

    @NotNull(message = "나이를 입력하세요." )
    private Integer age;

    @NotNull(message = "성별을 입력하세요." )
    private Gender gender;

    // 비밀번호 암호화하여 저장
    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .name(this.name)
                .age(this.age)
                .height(this.height)
                .gender(this.gender)
                .role(Role.USER)
                .build();
    }

    /**
     * Request 예시
     * {
     *     "loginId" : "하은하은@example.com",
     *     "password" : "password123",
     *     "passwordCheck" : "password123",
     *     "nickname" : "하은콩",
     *     "name" : "멈무",
     *     "height" : 161.1,
     *     "age" : 24,
     *     "gender" : "FEMALE"
     * }
     */
}

