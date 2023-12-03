package server.healthyFriends.sercurity.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;
    // JWT Token 발급
    public JwtTokenUtil(
            @Value("${jwt.secretKey}") final String secretKey,
            @Value("${jwt.token-validity-in-seconds}") final long accessTokenValiditySeconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }
    public String createAccessToken(String loginId) {
        return createToken(loginId, accessTokenValiditySeconds);
    }

    private String createToken(String loginId, long validitySeconds) {
        //Claim -> Jwt Token에 들어갈 정보
        //Claim에 loginId를 넣어줌 -> 나중에 loginId를 꺼낼 수 있음
        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(validitySeconds);


        return Jwts.builder()
                //토큰에 넣을 클레임(정보) 설정
                .setClaims(claims)
                //토큰 발급 시간
                .setIssuedAt(Date.from(now.toInstant()))
                //토큰 만료 시간
                .setExpiration(Date.from(tokenValidity.toInstant()))
                //토큰 서명 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)
                //문자열로 압축
                .compact();
    }

    // Claims에서 LoginId 추출
    public String getLoginId(String token) {
        return getClaims(token).getBody()
                .get("loginId", String.class);
    }

    // 발급된 Token의 만료 시간 초과 여부
    public boolean isExpired(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            Date expiredDate = claims.getBody()
                    .getExpiration();
            Date now = new Date();
            return expiredDate.after(now);
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("JWT token has expired", e);
        } catch (SecurityException
                 | MalformedJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new BadCredentialsException("Error validating JWT token", e);
        }
    }
    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

}
