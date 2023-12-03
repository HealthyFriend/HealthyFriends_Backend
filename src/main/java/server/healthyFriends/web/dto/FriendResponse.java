package server.healthyFriends.web.dto;

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
