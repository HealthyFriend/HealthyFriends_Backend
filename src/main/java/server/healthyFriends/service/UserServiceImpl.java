package server.healthyFriends.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.healthyFriends.converter.UserConverter;
import server.healthyFriends.domain.entity.Objective;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.sercurity.jwt.JwtTokenUtil;
import server.healthyFriends.repository.UserRepository;

import jakarta.transaction.Transactional;
import server.healthyFriends.web.dto.request.UserRequest;
import server.healthyFriends.web.dto.response.UserResponse;

import java.util.Optional;

import static server.healthyFriends.converter.UserConverter.toUser;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    //loginId 중복 체크
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    //닉네임 중복 체크
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

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

    public void withdrawal(Long userId, UserRequest.WithdrawalRequest req) {

        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        if(encoder.matches(req.getPassword(), user.getPassword())) {
            userRepository.delete(user);
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호가 올바르지 않습니다.");
        }

    }

    public void modifyUserInfo(Long userId, UserRequest.ModifyUserInfoRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 회원이 없습니다."));

        if(req.getAge()!=null) {
            user.setAge(req.getAge());
        }

        if(req.getName()!=null) {
            user.setName(req.getName());
        }

        if(req.getNickname()!=null) {
            user.setNickname(req.getNickname());
        }

        if(req.getHeight()!=null) {
            user.setHeight(req.getHeight());
        }

        if(req.getGender()!=null) {
            user.setGender(req.getGender());
        }

        if(req.getPassword()!=null) {
            user.setPassword(encoder.encode(req.getPassword()));
        }

        userRepository.save(user);
    }

    /**
     * userId(Long)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User getUserById(Long id) {
        if(id == null) return null;

        return userRepository.findById(id).orElse(null);
    }

    public User findById(Long id) {
        if(id == null) return null;

        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));
    }

    public User findByLoginId(String loginId) {
        if(loginId.equals(null)) return null;

        return userRepository.findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));
    }
}

