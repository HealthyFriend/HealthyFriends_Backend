package server.healthyFriends.web.dto.request.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {

    private String recipient_loginId;

}
