package server.healthyFriends.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import server.healthyFriends.sercurity.OAuth.CustomOauth2UserService;
import server.healthyFriends.sercurity.handler.JwtAccessDeniedHandler;
import server.healthyFriends.sercurity.handler.JwtAuthenticationEntryPoint;
import server.healthyFriends.sercurity.jwt.JwtTokenFilter;
import server.healthyFriends.service.user.UserService;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

//설정 클래스
@Configuration
//Spring Security의 웹 보안 지원 활성화, Spring MVC와 통합 제공
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenFilter jwtRequestFilter;
    private final CustomOauth2UserService customOauth2UserService;
    private final CorsConfig corsConfig;
    private final UserService userService;
/*
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
    }
*/
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
        //CSRF(Cross-Site Request Forgery) 보호 비활성화
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                    .rememberMe(RememberMeConfigurer::disable);

        //HTTP 기본 인증 비활성화
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
        );

        //JwtTokenFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilter(corsConfig.corsFilter());
        //권한 규칙 구성 시작
        httpSecurity.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/join").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/oauth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        //.requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/S3").permitAll()
                        .anyRequest().authenticated()
        );

        return httpSecurity.build();

    }
}
