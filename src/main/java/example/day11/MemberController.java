package example.day11;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController 
@RequestMapping("/api/member")
@RequiredArgsConstructor 
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService; // 1) Redis 전담 서비스 주입

    // [1] 회원가입
    @PostMapping("/signup")
    public boolean signup(@RequestBody MemberDto memberDto){
        return memberService.signup(memberDto);
    }

    // [2] 로그인 (JWT 발급 + Redis 저장 + 쿠키 응답)
    @PostMapping("/login")
    public MemberDto login(@RequestBody MemberDto memberDto, HttpServletResponse response) {
        // 1. 서비스 인증 확인
        MemberDto result = memberService.login(memberDto);
        if (result == null) return null; // 로그인 실패

        Long mno = result.getMno();

        // 2. 이중 토큰 생성 (Access: 30분, Refresh: 7일)
        String accessToken = jwtUtil.createAccessToken(mno);
        String refreshToken = jwtUtil.createRefreshToken(mno);

        // 3. RedisService를 통해 Refresh Token 안전하게 보관 (TTL: 7일)
        redisService.setRefreshToken(mno, refreshToken, JwtUtil.REFRESH_TOKEN_EXP_TIME);

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

    // [3] 내 정보 조회 (Access Token 쿠키 검증)
    @GetMapping("/me")
    public MemberDto getMyInfo(
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        if (accessToken == null) return null;

        Long mno = jwtUtil.getMnoFromToken(accessToken);
        if (mno == null) return null;

        return memberService.getMyInfo(mno);
    }

    // [4] 토큰 재발급 + RTR(Refresh Token Rotation)
    @PostMapping("/reissue")
    public boolean reissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        // 1. 쿠키 존재 여부 확인
        if (refreshToken == null) return false;

        // 2. 토큰 자체 유효성 및 mno 파싱
        Long mno = jwtUtil.getMnoFromToken(refreshToken);
        if (mno == null) return false;

        // 3. RedisService에서 저장된 원본 토큰 조회
        String savedRefreshToken = redisService.getRefreshToken(mno);

        // 4. 탈취 감지: 레디스에 없거나 전달받은 토큰과 다르면 침해로 간주 -> 레디스 토큰 파기
        if (savedRefreshToken == null || !refreshToken.equals(savedRefreshToken)) {
            redisService.deleteRefreshToken(mno); // 강제 로그아웃
            return false;
        }

        // 5. [RTR] 새로운 AccessToken 및 RefreshToken 생성
        String newAccessToken = jwtUtil.createAccessToken(mno);
        String newRefreshToken = jwtUtil.createRefreshToken(mno);

        // 6. Redis에 신규 RefreshToken 덮어쓰기 (기존 토큰 즉시 무효화)
        redisService.setRefreshToken(mno, newRefreshToken, JwtUtil.REFRESH_TOKEN_EXP_TIME);

        // 7. 브라우저 쿠키 갱신
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .path("/").maxAge(Duration.ofMinutes(30)).httpOnly(true).secure(false).sameSite("Lax").build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .path("/").maxAge(Duration.ofDays(7)).httpOnly(true).secure(false).sameSite("Lax").build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return true;
    }

    // [5] 로그아웃 (Redis 토큰 삭제 + 쿠키 파기)
    @PostMapping("/logout")
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
}

// package example.day11;

// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseCookie;
// import org.springframework.web.bind.annotation.*;

// import java.time.Duration;

// @RestController 
// @RequestMapping("/api/member")
// @RequiredArgsConstructor 
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// public class MemberController {

//     private final MemberService memberService;
//     private final JwtUtil jwtUtil; // 1) JWT 생성 및 검증 유틸 주입

//     // [1] 회원가입 (기존 유지)
//     @PostMapping("/signup")
//     public boolean signup(@RequestBody MemberDto memberDto){
//         return memberService.signup(memberDto);
//     }

//     // [2] 로그인 + 쿠키 + JWT Access Token 발급
//     @PostMapping("/login")
//     public MemberDto login(@RequestBody MemberDto memberDto, HttpServletResponse response) {
//         // 1. 서비스에게 인증 확인 (아이디/비밀번호 검증)
//         MemberDto result = memberService.login(memberDto);
//         if (result == null) return null; // 로그인 실패

//         // 2. 인증 성공 시 회원식별번호(mno)를 담은 위조 불가능한 JWT 토큰 생성
//         String accessToken = jwtUtil.createToken(result.getMno());

//         // 3. 발급된 JWT 토큰을 쿠키(login_token)에 안전하게 탑재
//         ResponseCookie cookie = ResponseCookie.from("login_token", accessToken)
//                 .path("/")                      // 전체 경로 전송 허용
//                 .maxAge(Duration.ofHours(1))    // 쿠키 만료시간 (JWT 만료시간과 동일하게 1시간 설정)
//                 .httpOnly(true)                 // XSS 방어: 브라우저 JS(document.cookie) 탈취 차단
//                 .secure(false)                  // 로컬 환경: false, 실무 HTTPS: true
//                 .sameSite("Lax")                // CSRF 방어
//                 .build();

//         // 4. Set-Cookie 응답 헤더로 클라이언트에 전송
//         response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

//         return result;
//     }

//     // [3] 내정보조회 + 쿠키에서 JWT 꺼내 검증
//     @GetMapping("/me")
//     public MemberDto getMyInfo(
//             // 브라우저가 전송한 'login_token' 쿠키 자동 주입
//             @CookieValue(value = "login_token", required = false) String token
//     ) {
//         // 1) 쿠키가 아예 없으면 비로그인 사용자 -> null 반환
//         if (token == null) return null;

//         // 2) 토큰 위변조 및 만료 여부 검증 후 회원번호(mno) 추출
//         Integer mno = jwtUtil.getMnoFromToken(token);
        
//         // 3) 토큰이 위조되었거나 만료되었으면 검증 실패(null)
//         if (mno == null) return null;

//         // 4) 검증된 회원번호로 회원 최신 정보 조회 후 반환
//         return memberService.getMyInfo(mno);
//     }

//     // [4] 로그아웃 + 쿠키 파기
//     @PostMapping("/logout")
//     public boolean logout(HttpServletResponse response) {
//         // 1) 만료시간을 0초로 설정한 만료 쿠키 생성
//         ResponseCookie cookie = ResponseCookie.from("login_token", "")
//                 .path("/")
//                 .maxAge(0)                      // 0초 만료 -> 브라우저가 수신 즉시 삭제
//                 .httpOnly(true)
//                 .secure(false)
//                 .build();

//         // 2) 응답 헤더에 담아 브라우저 쿠키 삭제
//         response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

//         return true;
//     }
// }