package server.healthyFriends.sercurity.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    // JWT Token 발급
    public static String createToken(String loginId, String key, long expireTime) {
        //Claim -> Jwt Token에 들어갈 정보
        //Claim에 loginId를 넣어줌 -> 나중에 loginId를 꺼낼 수 있음
        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);

        return Jwts.builder()
                //토큰에 넣을 클레임(정보) 설정
                .setClaims(claims)
                //토큰 발급 시간
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                //토큰 서명 설정
                .signWith(SignatureAlgorithm.HS256, key)
                //문자열로 압축
                .compact();
    }

    // Claims에서 LoginId 추출
    public static String getLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId").toString();
    }

    // 발급된 Token의 만료 시간 초과 여부
    public static boolean isExpired(String token, String secretKey) {
        Date expireDate = extractClaims(token, secretKey).getExpiration();

        return expireDate.before(new Date());
    }

    //SecretKey를 사용해 Token Parsing
    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
