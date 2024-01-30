package server.healthyFriends.converter;

import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

public class UserConverter {

    public static User toUser(UserRequest.JoinRequest request, String encodedPassword) {
        return User.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .name(request.getName())
                .age(request.getAge())
                .height(request.getHeight())
                .gender(request.getGender())
                .role(Role.USER)
                .build();
    }

    public static UserResponse.LoginResponse loginResponse(String accessToken) {
        return UserResponse.LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public static UserResponse.JoinResponse joinResponse(String accessToken) {
        return UserResponse.JoinResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public static UserResponse.UserInfoResponse userInfoResponse(User user) {
        return UserResponse.UserInfoResponse.builder()
                .age(user.getAge())
                .loginId(user.getLoginId())
                .name(user.getName())
                .nickname(user.getNickname())
                .height(user.getHeight())
                .build();
    }
}