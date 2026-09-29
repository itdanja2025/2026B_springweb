package example.day11;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component // SPRING MVC 패턴 객체가 아닌 일반 객체(빈) 생성 
public class JwtUtil {
    // @Value("${propertis파일내속성명}) , 속성값
    // propertis파일내 api인증키 또는 개발자 보안데이터들 넣어 안전하게 사용 목적
    @Value("${jwt.secret}")  
    private String key;
    // hmacSha 알고리즘 : 단방향 , 대칭키( 바이트로 변환 하여 설정 )
    private final SecretKey secretKey = Keys.hmacShaKeyFor( key.getBytes( StandardCharsets.UTF_8 ) );

    // [1] JWT 토큰 생성 메소드 
    public String createToken( Long mno ){
        String jwt = Jwts.builder() // 토큰 생성 시작
                    .subject( mno+"" ) // 토큰에 들어갈 내용(playload)들( 주로 식별번호, 권한 )
                    .issuedAt( new Date() ) // 토큰 생성 시간 ,   
                    .expiration( new Date( new Date().getTime() * 60 * 60 ) ) // 토큰 만료 시간 
                    // new Date() 현재시간 , new Date().getTime() 현재시간초 , * 60(1분) * 60 (1시간)
                    .signWith(secretKey) // 비밀키로 전자서명 
                    .compact(); // 토큰 생성 끝 , 토큰정보 문자열(String) 로 반환 
        System.out.println( jwt );
        return jwt;
    }

    // [2] JWT 토큰 검증 메소드
    public Long getMnoFromToken( String token ){
        try{  // 만약에 token 파싱(가져오기)이 실패이면 예외 발생한다.
            Claims claims = Jwts.parser() // 파싱
                            .verifyWith( secretKey ) // 전자서명 이용한 검증
                            .build()
                            .parseSignedClaims( token ) // 파싱할 토큰 
                            .getPayload(); // JWT 안에 payload 값 반환 
            Long mno = Long.parseLong( claims.getSubject() ) ; // payload 안에 subject 꺼내기 (문자열타입-->Long타입 변환 )
            System.out.println( mno );
            return mno; // 토큰 검증이 성공이면 회원번호 반환 
        }catch(Exception e ){
            return null; // 만약에 토큰이 없거나 문제가 있으면 null 반환 
        }
    }
}
