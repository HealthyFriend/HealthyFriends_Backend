package server.healthyFriends.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.healthyFriends.domain.enums.Role;
import server.healthyFriends.sercurity.jwt.JwtTokenFilter;
import server.healthyFriends.service.UserService;
import server.healthyFriends.service.UserServiceImpl;

//설정 클래스
@Configuration
//Spring Security의 웹 보안 지원 활성화, Spring MVC와 통합 제공
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private static String secretKey = "my-secret-key-20220121";

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용하여 비밀번호를 안전하게 저장
        return new BCryptPasswordEncoder();
    }

    // securityFilterChain 이름의 SecurityFilterChain 타입의 빈 반환
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /**
         * HTTP 기본 인증 비활성화
         * CSRF 보호 비활성화
         * 세션 관리 설정
         * JwtTokenFilter 추가
         * 권한 규칙 구성 추가
         * 특정 엔드포인트에 대한 인증 필요한 경우 -> 해당 엔드포인트에 대해 인증 필요함 설정
         * ADMIN 권한을 가진 사용자에게 특정 URL에 액세스할 권한 필요한 경우 -> 허용
         * 권한 규칙 구성 종료
         */
        return httpSecurity
                //HTTP 기본 인증 비활성화
                .httpBasic().disable()
                //CSRF(Cross-Site Request Forgery) 보호 비활성화
                .csrf().disable()
                //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //보안 구성 결합
                .and()
                //JwtTokenFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                //권한 규칙 구성 시작
                .authorizeRequests()
                ///jwt-login/info 엔드포인트에 대한 인증 필요
                //.antMatchers("/").authenticated()
                //ADMIN 권한 가진 사용자에게 /jwt-login/admin/ 하우의 모든 URL에 액세스할 권한 필요
                .antMatchers("/jwt-login/admin/**").hasAuthority(Role.ADMIN.name())
                .and().build();
    }
}
