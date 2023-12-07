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
}

