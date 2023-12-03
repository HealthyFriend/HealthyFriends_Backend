package server.healthyFriends;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtDecoderTest {
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTcwMTYxMTM4OSwiZXhwIjoxNzA1MjExMzg5fQ.TJVETyyrhk2olqCVxlsW1PyBP34OAq3h1L-P2lRHWAs";
        // 토큰을 디코딩하여 헤더, 페이로드, 시그니처를 출력
        String[] parts = token.split("\\.");
        String header = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String signature = parts[2];

        // 결과 출력
        System.out.println("Header: " + header);
        System.out.println("Payload: " + payload);
        System.out.println("Signature: " + signature);

        // 시크릿 키를 사용하여 토큰을 디코딩
        Claims claims = Jwts.parser().setSigningKey("MySecretKeyIs20220121PluSafasgdsgasgzsczgsgasgfsbzcbzbvzbnznd").parseClaimsJws(token).getBody();

        // 토큰의 클레임에서 필요한 정보 추출
        Long userId = claims.get("userId", Long.class);

        // 결과 출력
        System.out.println("Decoded ID: " + userId);
    }
}
