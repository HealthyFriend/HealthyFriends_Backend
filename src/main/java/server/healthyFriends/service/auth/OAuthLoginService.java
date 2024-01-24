package server.healthyFriends.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.sercurity.OAuth.Basic.OAuthInfoResponse;
import server.healthyFriends.sercurity.OAuth.Basic.OAuthLoginParams;
import server.healthyFriends.sercurity.OAuth.Basic.RequestOAuthInfoService;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.web.dto.response.UserResponse;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(userId);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByLoginId(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
        User user = User.builder()
                .loginId(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                //.oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return userRepository.save(user).getId();
    }
}
