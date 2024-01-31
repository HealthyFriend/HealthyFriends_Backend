package server.healthyFriends.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

public class UserResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        String accessToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponse {
        String accessToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoResponse {
        String name;
        String nickname;
        BigDecimal height;
        Integer age;
        String loginId;
    }

    //프론트 요청으로 만든 Test 용도 Response
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResponse {
        String name;
        String nickname;
        BigDecimal height;
        Integer age;
        String loginId;
        String accessToken;
    }
}
