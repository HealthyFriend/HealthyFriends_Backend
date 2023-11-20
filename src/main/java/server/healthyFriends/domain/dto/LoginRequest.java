package server.healthyFriends.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String loginId;
    private String password;

}
