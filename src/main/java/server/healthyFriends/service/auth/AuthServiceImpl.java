package server.healthyFriends.service.auth;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.healthyFriends.converter.UserConverter;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

import static server.healthyFriends.converter.UserConverter.toUser;
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    // 회원가입
    public void join(UserRequest.JoinRequest req) {
        userRepository.save(toUser(req, encoder.encode(req.getPassword())));
    }

    /**
     *  로그인 기능
     *  화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 User return
     *  loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    public UserResponse.LoginResponse login(UserRequest.LoginRequest req) {
        User user = userRepository.findByLoginId(req.getLoginId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenUtil.createAccessToken(user.getId());

        redisTemplate.opsForValue().set("JWT_TOKEN:" + user.getId(),accessToken);

        return UserConverter.loginResponse(accessToken);
    }

    public void logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 여기서 userDetails에서 필요한 정보 추출
            String userId = userDetails.getUsername();

            redisTemplate.delete("JWT_TOKEN:" + userId);
        }
    }

}
