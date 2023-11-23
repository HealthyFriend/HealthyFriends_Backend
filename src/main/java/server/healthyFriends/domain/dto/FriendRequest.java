package server.healthyFriends.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequest {

    private Long id;

    private Long requester_id;

    private Long recipient_id;

}
