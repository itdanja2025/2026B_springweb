package example.day12_;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/member")
@RequiredArgsConstructor 
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
        private final RedisService redisService; // 1) Redis 전담 서비스 주입


    // [1] 회원가입 + 기존 유지
    @PostMapping("/signup")
    public boolean signup( @RequestBody MemberDto memberDto ){
        return memberService.signup( memberDto );
    }

    // [2] 로그인 + 쿠키변경( 회원 식별(번호) 쿠키에 담아 클라이언트에 전송 )
    @PostMapping("/login")
    public MemberDto login( @RequestBody MemberDto memberDto , HttpServletResponse response ){
        // 1. 서비스 에게 인증/로그인 확인 (기존 유지)
        MemberDto result = memberService.login(memberDto);
        if( result == null ) return null; // 로그인 실패시 
        // 2. 로그인 성공 시 쿠키 생성/발급 *********** 쿠키 값을 jwt 안전하게 변경 *************
        // 4. 토큰(token) 발급 요청
        // 2. 이중 토큰 생성 (Access: 30분, Refresh: 7일)
        String accessToken = jwtUtil.createToken(result.getMno());
        String refreshToken = jwtUtil.createRefreshToken(result.getMno());

        // 3. RedisService를 통해 Refresh Token 안전하게 보관 (TTL: 7일)
        redisService.setRefreshToken( result.getMno() , refreshToken );

        // 4. 쿠키 생성 (HttpOnly, Lax)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .path("/").maxAge(Duration.ofMinutes(30)).httpOnly(true).secure(false).sameSite("Lax").build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/").maxAge(Duration.ofDays(7)).httpOnly(true).secure(false).sameSite("Lax").build();

        // 5. 응답 헤더 탑재 (addHeader로 2개 쿠키 전송)
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return result;
    }

    // [3] 내정보조회 + 쿠키
    @GetMapping("/me")
    public MemberDto getMyInfo( 
        // @CookieValue( value="쿠키명") ){ // 요청한 브라우저의 쿠키 가져오기 
        @CookieValue (value="accessToken" , required = false ) String token ){
            System.out.println( token );
        //1. 만약에 token 가 없다면 비로그인
        if( token == null ) return  null;
        // ********* 쿠키에 저장된 token 이용하여 회원번호 찾기 ************
        Long loginMno = jwtUtil.getMnoFromToken(token);
        // 2. 로그인 중이면 서비스에게 회원정보 요청
        return memberService.getMyInfo( loginMno );
    }

    // [4] 로그아웃 + 쿠키 
    @PostMapping ("/logout")
    public boolean logout(
                    @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response
    ) {
        // 1. 현재 토큰에서 회원식별 번호를 얻을 수 있다면 Redis의 Refresh Token 삭제
        if (accessToken != null) {
            Long mno = jwtUtil.getMnoFromToken(accessToken);
            if (mno != null) {
                redisService.deleteRefreshToken(mno);
            }
        }

        // 2. 브라우저 쿠키 2개 만료(0초) 처리
        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .path("/").maxAge(0).httpOnly(true).secure(false).build();

        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .path("/").maxAge(0).httpOnly(true).secure(false).build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());

        return true;
    }

    // [4] 토큰 재발급 + RTR(Refresh Token Rotation)
    @PostMapping("/reissue")
    public MemberDto reissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        // 1. 쿠키 존재 여부 확인
        if (refreshToken == null) return null;

        // 2. 토큰 자체 유효성 및 mno 파싱
        Long mno = jwtUtil.getMnoFromToken(refreshToken);
        if (mno == null) return null;

        // 3. RedisService에서 저장된 원본 토큰 조회
        String savedRefreshToken = redisService.getRefreshToken(mno);

        // 4. 탈취 감지: 레디스에 없거나 전달받은 토큰과 다르면 침해로 간주 -> 레디스 토큰 파기
        if (savedRefreshToken == null || !refreshToken.equals(savedRefreshToken)) {
            redisService.deleteRefreshToken(mno); // 강제 로그아웃
            return null;
        }

        // 5. [RTR] 새로운 AccessToken 및 RefreshToken 생성
        String newAccessToken = jwtUtil.createToken(mno);
        String newRefreshToken = jwtUtil.createRefreshToken(mno);

        // 6. Redis에 신규 RefreshToken 덮어쓰기 (기존 토큰 즉시 무효화)
        redisService.setRefreshToken(mno, newRefreshToken);

        // 7. 브라우저 쿠키 갱신
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .path("/").maxAge(Duration.ofMinutes(30)).httpOnly(true).secure(false).sameSite("Lax").build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .path("/").maxAge(Duration.ofDays(7)).httpOnly(true).secure(false).sameSite("Lax").build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return memberService.getMyInfo(mno);
    }
}
