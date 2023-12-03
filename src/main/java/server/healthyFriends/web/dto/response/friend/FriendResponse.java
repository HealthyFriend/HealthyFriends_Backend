package server.healthyFriends.web.dto.response.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendResponse {

    private Long id;

    private Long requester_id;

    private Long recipient_id;

}
