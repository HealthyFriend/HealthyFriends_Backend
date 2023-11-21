package server.healthyFriends.sercurity.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import server.healthyFriends.domain.entity.User;
import server.healthyFriends.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
//extends OncePerRequestFilter -> 한 번의 요청에 한 번만 실행되도록 보장
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    // JWT 토큰 검증, 사용자 인증 로직 구현
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //HTTP 요청의 Header에서 Authorization 값 가져옴
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Header의 Authorization 값이 Null -> Jwt Token을 전송 X => 로그인 X
        if(authorizationHeader == null) {
            // 필터 작업 완료
            filterChain.doFilter(request, response);
            return;
        }

        // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 -> 잘못된 토큰
        // HTTP 헤더 -> Authorization: Bearer fdbafkjbagsagsag...(JWT)
        if(!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
        String token = authorizationHeader.split(" ")[1];

        // 전송받은 Jwt Token이 만료 -> 인증 X
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Jwt Token에서 loginId 추출
        String loginId = JwtTokenUtil.getLoginId(token, secretKey);

        // 추출한 loginId로 User 찾아오기
        User loginUser = userService.getLoginUserByLoginId(loginId);

        // jWT 토큰 사용하여 loginUser 정보로 UsernamePasswordAuthenticationToken 생성, 권한 설정
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                //첫번쩨 매개변수 : 사용자의 Id를 토큰에 설정,
                //두번째 매개변수 : 패스워드, 사용 안해서 null로
                //세번째 매개변수 : 사용자 권한 정보를 나타내는 SimpleGrantedAuthority 객체 생성, 이를 리스트로 묶어서 토큰에 설정
                //loginUser.getRole().name()은 사용자의 Role을 문자열로 가져와서 해당 역할 부여
                loginUser.getId(), null, List.of(new SimpleGrantedAuthority(loginUser.getRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 인증 정보 설정 -> 권한 부여
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
