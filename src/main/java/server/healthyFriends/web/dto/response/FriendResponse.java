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
        private String profileImageUrl;
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
    public static class MappingFriendResponse {
        private Long friendId;
        private Long friendMappingId;
        private String friendName;
        private String friendLoginId;
        private String friendNickname;
        private Boolean isFriend;
        private String profileImageUrl;
        private String objectiveHead;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProspectiveFriendResponse {
        private Long friendId;
        private Long friendMappingId;
        private String friendName;
        private String friendLoginId;
        private String friendNickname;
        private Boolean isFriend;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendInfo {
        private Long friendId;
        private String nickname;
        private String objectiveHead;
        private String profileImageUrl;
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
