package server.healthyFriends.sercurity.jwt;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import server.healthyFriends.config.SecurityConfig;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
//extends OncePerRequestFilter -> 한 번의 요청에 한 번만 실행되도록 보장
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final CustomUserDetailsImpl customUserDetailsImpl;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    // JWT 토큰 검증, 사용자 인증 로직 구현
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //HTTP 요청의 Header에서 Authorization 값 가져옴
        String authorizationHeader = request.getHeader("AUTHORIZATION");

        // Header의 Authorization 값이 Null -> Jwt Token을 전송 X => 로그인 X
        // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 -> 잘못된 토큰
        // HTTP 헤더 -> Authorization: Bearer fdbafkjbagsagsag...(JWT)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);


            if (jwtTokenUtil.isExpired(token)) {
                // Jwt Token에서 loginId 추출
                Long userId = jwtTokenUtil.getUserId(token);
                UserDetails userDetails = customUserDetailsImpl.loadUserByUsername(userId.toString());

                if (userDetails != null) {
                    //첫번쩨 매개변수 : userDetails
                    //두번째 매개변수 : 패스워드, 사용 안해서 null로
                    //세번째 매개변수 : 사용자 권한 정보를 나타내는 SimpleGrantedAuthority 객체 생성, 이를 리스트로 묶어서 토큰에 설정
                    //loginUser.getRole().name()은 사용자의 Role을 문자열로 가져와서 해당 역할 부여
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new EntityNotFoundException("해당하는 유저가 업습니다.");
                }
            } else {
                throw new BadCredentialsException("유효하지 않은 토큰입니다.");
            }
        }
        filterChain.doFilter(request, response);
    }

}
