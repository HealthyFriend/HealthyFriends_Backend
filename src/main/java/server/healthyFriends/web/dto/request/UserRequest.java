package server.healthyFriends.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.healthyFriends.domain.enums.Gender;

import java.math.BigDecimal;

public class UserRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {

        @JsonProperty("login_id")
        private String loginId;
        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class JoinRequest {

        @NotBlank(message = "아이디를 입력하세요.")
        private String loginId;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
        private String passwordCheck;

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @NotBlank(message = "닉네임을 입력하세요.")
        private String nickname;

        @NotNull(message = "키를 입력하세요.")
        private BigDecimal height;

        @NotNull(message = "나이를 입력하세요.")
        private Integer age;

        @NotNull(message = "성별을 입력하세요.")
        private Gender gender;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class WithdrawalRequest {
        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }
}
