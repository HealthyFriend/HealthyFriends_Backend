package server.healthyFriends.web.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @JsonProperty("login_id")
    private String loginId;
    private String password;

}
