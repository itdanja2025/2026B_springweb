package example.day11;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_STRING = "your-very-secure-and-long-secret-key-must-be-32bytes!";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // 유효시간 정의 (밀리초 단위)
    public static final long ACCESS_TOKEN_EXP_TIME = 1000L * 60 * 30;         // 30분
    public static final long REFRESH_TOKEN_EXP_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    // [1] Access Token 발급 (회원 식별 번호 mno 저장)
    public String createAccessToken(Long mno) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(mno))
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_TOKEN_EXP_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // [2] Refresh Token 발급 (재발급 검증용)
    public String createRefreshToken(Long mno) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(mno))
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + REFRESH_TOKEN_EXP_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // [3] 토큰 검증 및 회원 식별값(mno) 추출
    public Long getMnoFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            // 위조되었거나 만료된 경우
            return null;
        }
    }
}


// package example.day11;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import java.nio.charset.StandardCharsets;
// import java.util.Date;

// @Component
// public class JwtUtil {

//     // 1. 서명에 사용할 비밀키 (최소 256비트 / 32바이트 이상이어야 함)
//     // 실무에서는 application.properties에서 주입받는 것이 안전함
//     private final String SECRET_STRING = "your-very-secure-and-long-secret-key-must-be-32bytes!";
//     private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

//     // 토큰 유효시간 (예: 1시간 = 1000ms * 60s * 60m)
//     private final long EXPIRATION_TIME = 1000L * 60 * 60;

//     // [1] 토큰 발급 (로그인 성공 시 호출)
//     public String createToken(Long mno) {
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

//         return Jwts.builder()
//                 .subject(String.valueOf(mno))          // 토큰의 주체(Subject)로 회원 식별번호(mno) 저장
//                 .issuedAt(now)                         // 토큰 발급 시간
//                 .expiration(expiryDate)                // 토큰 만료 시간
//                 .signWith(SECRET_KEY)                  // 서버 비밀키로 전자서명 (위조 방지)
//                 .compact();                            // 암호화된 토큰 문자열(Header.Payload.Signature) 반환
//     }

//     // [2] 토큰 검증 및 회원 식별값(mno) 추출 (내정보조회, 인증 필요 요청 시 호출)
//     public Integer getMnoFromToken(String token) {
//         try {
//             // 1) 서버 비밀키로 토큰의 서명(Signature) 및 만료시간(Expiration) 검증
//             Claims claims = Jwts.parser()
//                     .verifyWith(SECRET_KEY)
//                     .build()
//                     .parseSignedClaims(token)
//                     .getPayload();

//             // 2) 토큰에서 subject로 담았던 회원 식별자 추출 후 Integer 반환
//             return Integer.parseInt(claims.getSubject());
//         } catch (Exception e) {
//             // 서명이 일치하지 않거나(위조), 토큰 유효시간이 만료된 경우 예외 발생 -> null 반환
//             System.out.println("토큰 검증 실패: " + e.getMessage());
//             return null;
//         }
//     }
// }