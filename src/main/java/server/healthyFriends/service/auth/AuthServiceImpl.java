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
import server.healthyFriends.service.exercise.ExerciseService;
import server.healthyFriends.web.dto.request.ExerciseRequest;
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
    private final ExerciseService exerciseService;
    private static final String defaultImageUrl = "https://hf-bucket-1.s3.ap-northeast-2.amazonaws.com/static/hf-images/74a9ee1b-dc0b-432f-841c-675217f59337";

    // 회원가입
    //public UserResponse.JoinResponse join(UserRequest.JoinRequest req) {
    public UserResponse.TestResponse join(UserRequest.JoinRequest req) {

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

        user.setProfileImageUrl(defaultImageUrl);

        User savedUser = userRepository.save(user);

        addDefaultExercise(savedUser);

        String accessToken = jwtTokenUtil.createAccessToken(user.getId());

        redisTemplate.opsForValue().set("JWT_TOKEN:" + user.getId(),accessToken);

        return UserConverter.testResponse(user,accessToken);
        //return UserConverter.joinResponse(accessToken);
    }

    // 로그인
    //public UserResponse.LoginResponse login(UserRequest.LoginRequest req) {
    public UserResponse.TestResponse login(UserRequest.LoginRequest req) {
        User user = userRepository.findByLoginId(req.getLoginId()).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenUtil.createAccessToken(user.getId());

        redisTemplate.opsForValue().set("JWT_TOKEN:" + user.getId(),accessToken);

        return UserConverter.testResponse(user,accessToken);
        //return UserConverter.loginResponse(accessToken);
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

    // 랜덤닉네임 생성
    private String generateRandomNickname() {
        return "HelfUser" + UUID.randomUUID().toString().replaceAll("-","").substring(0,7);
    }

    // 기본 운동 추가
    private void addDefaultExercise(User user) {
        ExerciseRequest.addExerciseRequest defaultExercise1 = new ExerciseRequest.addExerciseRequest();
        defaultExercise1.setExerciseCategory("하체");
        defaultExercise1.setExerciseName("바벨 백스쿼트");
        exerciseService.addExercise(user.getId(), defaultExercise1);

        ExerciseRequest.addExerciseRequest defaultExercise2 = new ExerciseRequest.addExerciseRequest();
        defaultExercise2.setExerciseCategory("하체");
        defaultExercise2.setExerciseName("프론트 스쿼트");
        exerciseService.addExercise(user.getId(), defaultExercise2);

        ExerciseRequest.addExerciseRequest defaultExercise3 = new ExerciseRequest.addExerciseRequest();
        defaultExercise3.setExerciseCategory("하체");
        defaultExercise3.setExerciseName("런지");
        exerciseService.addExercise(user.getId(), defaultExercise3);

        ExerciseRequest.addExerciseRequest defaultExercise4= new ExerciseRequest.addExerciseRequest();
        defaultExercise4.setExerciseCategory("하체");
        defaultExercise4.setExerciseName("루마니안 데드리프트");
        exerciseService.addExercise(user.getId(), defaultExercise4);

        ExerciseRequest.addExerciseRequest defaultExercise5= new ExerciseRequest.addExerciseRequest();
        defaultExercise5.setExerciseCategory("하체");
        defaultExercise5.setExerciseName("레그컬");
        exerciseService.addExercise(user.getId(), defaultExercise5);

        ExerciseRequest.addExerciseRequest defaultExercise6= new ExerciseRequest.addExerciseRequest();
        defaultExercise6.setExerciseCategory("하체");
        defaultExercise6.setExerciseName("레그프레스");
        exerciseService.addExercise(user.getId(), defaultExercise6);

        ExerciseRequest.addExerciseRequest defaultExercise21= new ExerciseRequest.addExerciseRequest();
        defaultExercise21.setExerciseCategory("가슴");
        defaultExercise21.setExerciseName("벤치 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise21);

        ExerciseRequest.addExerciseRequest defaultExercise22= new ExerciseRequest.addExerciseRequest();
        defaultExercise22.setExerciseCategory("가슴");
        defaultExercise22.setExerciseName("인클라인 벤치 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise22);

        ExerciseRequest.addExerciseRequest defaultExercise23= new ExerciseRequest.addExerciseRequest();
        defaultExercise23.setExerciseCategory("가슴");
        defaultExercise23.setExerciseName("딥스");
        exerciseService.addExercise(user.getId(), defaultExercise23);

        ExerciseRequest.addExerciseRequest defaultExercise24= new ExerciseRequest.addExerciseRequest();
        defaultExercise24.setExerciseCategory("가슴");
        defaultExercise24.setExerciseName("체스트 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise24);

        ExerciseRequest.addExerciseRequest defaultExercise25= new ExerciseRequest.addExerciseRequest();
        defaultExercise25.setExerciseCategory("가슴");
        defaultExercise25.setExerciseName("펙덱 플라이");
        exerciseService.addExercise(user.getId(), defaultExercise25);

        ExerciseRequest.addExerciseRequest defaultExercise31= new ExerciseRequest.addExerciseRequest();
        defaultExercise31.setExerciseCategory("등");
        defaultExercise31.setExerciseName("풀업");
        exerciseService.addExercise(user.getId(), defaultExercise31);

        ExerciseRequest.addExerciseRequest defaultExercise32= new ExerciseRequest.addExerciseRequest();
        defaultExercise32.setExerciseCategory("등");
        defaultExercise32.setExerciseName("랫풀다운");
        exerciseService.addExercise(user.getId(), defaultExercise32);

        ExerciseRequest.addExerciseRequest defaultExercise33= new ExerciseRequest.addExerciseRequest();
        defaultExercise33.setExerciseCategory("등");
        defaultExercise33.setExerciseName("바벨 로우");
        exerciseService.addExercise(user.getId(), defaultExercise33);

        ExerciseRequest.addExerciseRequest defaultExercise34= new ExerciseRequest.addExerciseRequest();
        defaultExercise34.setExerciseCategory("등");
        defaultExercise34.setExerciseName("시티드 로우");
        exerciseService.addExercise(user.getId(), defaultExercise34);

        ExerciseRequest.addExerciseRequest defaultExercise35= new ExerciseRequest.addExerciseRequest();
        defaultExercise35.setExerciseCategory("등");
        defaultExercise35.setExerciseName("티바 로우");
        exerciseService.addExercise(user.getId(), defaultExercise35);

        ExerciseRequest.addExerciseRequest defaultExercise36= new ExerciseRequest.addExerciseRequest();
        defaultExercise36.setExerciseCategory("등");
        defaultExercise36.setExerciseName("케이블 암 풀다운");
        exerciseService.addExercise(user.getId(), defaultExercise36);

        ExerciseRequest.addExerciseRequest defaultExercise41= new ExerciseRequest.addExerciseRequest();
        defaultExercise41.setExerciseCategory("어깨");
        defaultExercise41.setExerciseName("오버 헤드 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise41);

        ExerciseRequest.addExerciseRequest defaultExercise42= new ExerciseRequest.addExerciseRequest();
        defaultExercise42.setExerciseCategory("어깨");
        defaultExercise42.setExerciseName("밀리터리 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise42);

        ExerciseRequest.addExerciseRequest defaultExercise43= new ExerciseRequest.addExerciseRequest();
        defaultExercise43.setExerciseCategory("어깨");
        defaultExercise43.setExerciseName("사이드 레터럴 레이즈");
        exerciseService.addExercise(user.getId(), defaultExercise43);

        ExerciseRequest.addExerciseRequest defaultExercise44= new ExerciseRequest.addExerciseRequest();
        defaultExercise44.setExerciseCategory("어깨");
        defaultExercise44.setExerciseName("벤트 오버 덤벨 레터럴 레이즈");
        exerciseService.addExercise(user.getId(), defaultExercise44);

        ExerciseRequest.addExerciseRequest defaultExercise45= new ExerciseRequest.addExerciseRequest();
        defaultExercise45.setExerciseCategory("어깨");
        defaultExercise45.setExerciseName("슈러그");
        exerciseService.addExercise(user.getId(), defaultExercise45);

        ExerciseRequest.addExerciseRequest defaultExercise46= new ExerciseRequest.addExerciseRequest();
        defaultExercise46.setExerciseCategory("어깨");
        defaultExercise46.setExerciseName("덤벨 숄더 프레스");
        exerciseService.addExercise(user.getId(), defaultExercise46);

        ExerciseRequest.addExerciseRequest defaultExercise51= new ExerciseRequest.addExerciseRequest();
        defaultExercise51.setExerciseCategory("팔");
        defaultExercise51.setExerciseName("덤벨컬");
        exerciseService.addExercise(user.getId(), defaultExercise51);

        ExerciseRequest.addExerciseRequest defaultExercise52= new ExerciseRequest.addExerciseRequest();
        defaultExercise52.setExerciseCategory("팔");
        defaultExercise52.setExerciseName("해머컬");
        exerciseService.addExercise(user.getId(), defaultExercise52);

        ExerciseRequest.addExerciseRequest defaultExercise53= new ExerciseRequest.addExerciseRequest();
        defaultExercise53.setExerciseCategory("팔");
        defaultExercise53.setExerciseName("라잉 트라이 익스텐션");
        exerciseService.addExercise(user.getId(), defaultExercise53);

        ExerciseRequest.addExerciseRequest defaultExercise54= new ExerciseRequest.addExerciseRequest();
        defaultExercise54.setExerciseCategory("팔");
        defaultExercise54.setExerciseName("바벨컬");
        exerciseService.addExercise(user.getId(), defaultExercise54);

        ExerciseRequest.addExerciseRequest defaultExercise55= new ExerciseRequest.addExerciseRequest();
        defaultExercise55.setExerciseCategory("팔");
        defaultExercise55.setExerciseName("케이블 푸시 다운");
        exerciseService.addExercise(user.getId(), defaultExercise55);

        ExerciseRequest.addExerciseRequest defaultExercise61= new ExerciseRequest.addExerciseRequest();
        defaultExercise61.setExerciseCategory("복근");
        defaultExercise61.setExerciseName("복근 롤아웃");
        exerciseService.addExercise(user.getId(), defaultExercise61);

        ExerciseRequest.addExerciseRequest defaultExercise62 = new ExerciseRequest.addExerciseRequest();
        defaultExercise62.setExerciseCategory("복근");
        defaultExercise62.setExerciseName("크런치");
        exerciseService.addExercise(user.getId(), defaultExercise62);

        ExerciseRequest.addExerciseRequest defaultExercise63 = new ExerciseRequest.addExerciseRequest();
        defaultExercise63.setExerciseCategory("복근");
        defaultExercise63.setExerciseName("레그 레이즈");
        exerciseService.addExercise(user.getId(), defaultExercise63);

        ExerciseRequest.addExerciseRequest defaultExercise64 = new ExerciseRequest.addExerciseRequest();
        defaultExercise64.setExerciseCategory("복근");
        defaultExercise64.setExerciseName("행잉 레그 레이즈");
        exerciseService.addExercise(user.getId(), defaultExercise64);

        ExerciseRequest.addExerciseRequest defaultExercise65 = new ExerciseRequest.addExerciseRequest();
        defaultExercise65.setExerciseCategory("복근");
        defaultExercise65.setExerciseName("플랭크");
        exerciseService.addExercise(user.getId(), defaultExercise65);

    }

}
