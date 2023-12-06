package server.healthyFriends.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {
    @Schema(description = "친구 신청 대상의 로그인ID", nullable = false, example = "하은@example.com")
    private String recipient_loginId;

}
