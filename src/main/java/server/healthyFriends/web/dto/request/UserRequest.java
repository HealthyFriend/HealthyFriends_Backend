package server.healthyFriends.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import server.healthyFriends.domain.enums.Gender;

import java.math.BigDecimal;

public class UserRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {

        @JsonProperty("login_id")
        @Size(min = 6, max=20)
        @Schema(description = "로그인 ID", nullable = false, example = "더구더구@example.com")
        private String loginId;
        @Size(min = 8, max=20)
        @Schema(description = "비밀번호", nullable = false, example = "password123")
        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class JoinRequest {

        @NotBlank(message = "6~20글자의 아이디를 입력하세요.")
        @Size(min = 6, max=20)
        @Schema(description = "로그인 ID", nullable = false, example = "더구더구@example.com1")
        private String loginId;

        @NotBlank(message = "8~20글자의 비밀번호를 입력하세요.")
        @Size(min = 8, max=20)
        @Schema(description = "비밀번호", nullable = false, example = "password1231")
        private String password;
        private String passwordCheck;

        @NotBlank(message = "2~4글자의 이름을 입력하세요.")
        @Size(min=2, max=4)
        @Schema(description = "이름", nullable = false, example = "윤덕우1")
        private String name;

        //@NotBlank(message = "2~10글자의 닉네임을 입력하세요.")
        @Size(min=2,max=10)
        @Schema(description = "닉네임", nullable = true, example = "더구더구1")
        private String nickname;

        //@NotNull(message = "키를 입력하세요.(140cm~200cm)")
        @DecimalMin(value="140.0") @DecimalMax(value="200.0")
        @Schema(description = "키", nullable = true, example = "178.3")
        private BigDecimal height;

        @NotNull(message = "나이를 입력하세요.(12세~99세)")
        @Min(value=12) @Max(value=99)
        @Schema(description = "나이", nullable = false, example = "27")
        private Integer age;

        @NotNull(message = "성별을 입력하세요.(MALE, FEMALE")
        @Enumerated(EnumType.STRING)
        @Schema(description = "성별", nullable = false, example = "MALE")
        private Gender gender;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class WithdrawalRequest {
        @NotBlank(message = "비밀번호를 입력하세요.")
        @Size(min = 8, max=20)
        @Schema(description = "비밀번호", nullable = false, example = "password1231")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ModifyUserInfoRequest {
        @Size(min=3, max=4, message = "이름 제한 : 2~4 글자")
        @Schema(description = "이름", nullable = true, example = "윤덕우1")
        private String name;
        @Size(min=2,max=10, message = "닉네임 제한 : 2~10 글자")
        @Schema(description = "닉네임", nullable = true, example = "더구더구1")
        private String nickname;
        @Min(value=12) @Max(value=99)
        @Schema(description = "나이", nullable = true, example = "27")
        private Integer age;
        @DecimalMin(value="140.0") @DecimalMax(value="200.0")
        @Schema(description = "키", nullable = true, example = "178.3")
        private BigDecimal height;
        @Size(min = 8, max=20, message = "비밀번호 제한 : 8~20 글자")
        @Schema(description = "비밀번호", nullable = true, example = "password1231")
        private String password;
        @Enumerated(EnumType.STRING)
        @Schema(description = "성별", nullable = true, example = "MALE")
        private Gender gender;
    }
}
