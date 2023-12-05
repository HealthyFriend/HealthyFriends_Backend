package server.healthyFriends;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtDecoderTest {
    public static void main(String[] args) {
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTcwMTc5MzE1MiwiZXhwIjoxNzA1MzkzMTUyfQ.88BknIrDqEYbFUWaVZAW3GsfJrnmfsVlQYIVNv7vWtk";

      String sk = "MySecretKeyIs20220121PluSafasgdsgasgzsczgsgasgfsbzcbzbvzbnznd";
      SecretKey secretKey = Keys.hmacShaKeyFor(sk.getBytes(StandardCharsets.UTF_8));

        // JWT 토큰 검증
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey) // 토큰을 서명한 키 (Secret Key)
                    .parseClaimsJws(removeBearerPrefix(jwtToken)); // "Bearer " 제거

            // 토큰이 정상적으로 검증되었을 때의 로직
            Claims body = claimsJws.getBody();
            System.out.println("User ID: " + body.get("userId"));
        } catch (Exception e) {
            // 토큰 검증 실패 시의 예외 처리
            e.printStackTrace();
        }
    }

    private static String removeBearerPrefix(String token) {
        // "Bearer " 부분을 제거하여 실제 토큰 값만 반환
        return token.replace("Bearer ", "");
    }
}
