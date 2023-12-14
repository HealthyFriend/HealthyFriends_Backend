package server.healthyFriends.service.auth;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.apiPayload.ResponseUtil;
import server.healthyFriends.converter.UserConverter;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.repository.UserRepository;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

import java.time.Instant;
import java.util.UUID;

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
    public UserResponse.JoinResponse join(UserRequest.JoinRequest req) {

        User user = UserConverter.toUser(req, encoder.encode(req.getPassword()));

        if(userRepository.existsByNickname(user.getNickname())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 존재하는 닉네임입니다.");
        }

        if(userRepository.existsByLoginId(user.getLoginId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 존재하는 아이디입니다.");
        }

        if (!req.getPassword().equals(req.getPasswordCheck())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if(req.getNickname()==null || req.getNickname().isEmpty()) {
            user.setNickname(generateRandomNickname());
        }

        userRepository.save(user);

        String accessToken = jwtTokenUtil.createAccessToken(user.getId());

        redisTemplate.opsForValue().set("JWT_TOKEN:" + user.getId(),accessToken);

        return UserConverter.joinResponse(accessToken);
    }

    // 로그인
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

    //로그아웃
    public void logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 여기서 userDetails에서 필요한 정보 추출
            String userId = userDetails.getUsername();

            redisTemplate.delete("JWT_TOKEN:" + userId);
        }
    }

    private String generateRandomNickname() {
        return "HelfUser" + UUID.randomUUID().toString().replaceAll("-","").substring(0,7);
    }

}
