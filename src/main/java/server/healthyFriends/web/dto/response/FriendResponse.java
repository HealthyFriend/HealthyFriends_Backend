package server.healthyFriends.web.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


public class FriendResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindFriendResponse {
        private String friendLoginId;
        private String friendName;
        private String friendNickname;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestFriendResponse {
        private Long friendMappingId;
        private Long requesterId;
        private Long recipientId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptFriendResponse {
        private Long friendMappingId;
        private Long requesterId;
        private Long recipientId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendInfo {
        private String nickname;
        private String objectiveHead;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListFriendResponse {
        List<FriendResponse.FriendInfo> friendInfoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendObjective {
        private String nickname;
        private String objectiveHead;
        private String objectiveBody;
        private LocalDate endDay;
        private LocalDate startDay;
    }


}
