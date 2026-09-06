package example.day09_0910;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JWTController {
    private final JWTService jwtService;

    // [1] JWT토큰생성 == 데이터를 암호화 하기 == 로그인정보 vs 세션(HTTP)
    @GetMapping("/create") // http://localhost:8080/api/jwt/create?data=바나나
    public ResponseEntity<?> 토큰생성(  @RequestParam String data ){
        return ResponseEntity.ok( jwtService.토큰생성( data ) );
    }

    // [2] JWT토큰 값 추출 == 암호화된 JWT 토큰을 다시 평문으로
    @GetMapping("/read") // http://localhost:8080/api/jwt/read?토큰=eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoi67CU64KY64KYIiwiaWF0IjoxNzc0MzI2NjAxLCJleHAiOjE3NzQzMjY2MjF9.2ONgqHwPWdHF24CwhRsVBmXL104GkG9WpqU_NH874w8
    public ResponseEntity<?> 값추출( @RequestParam String 토큰 ){
        return ResponseEntity.ok( jwtService.값추출( 토큰 ) );
    }
}











