package example.day12;

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

    // [1] 회원가입 + 기존 유지
    @PostMapping("/signup")
    public boolean signup( @RequestBody MemberDto memberDto ){
        return memberService.signup( memberDto );
    }

    private final RedisTokenService redisTokenService;
    // [2] 로그인 
    @PostMapping("/login")
    public MemberDto login( @RequestBody MemberDto memberDto , HttpServletResponse response ){
        MemberDto result = memberService.login(memberDto); // 1. 서비스 에게 인증/로그인 확인 (기존 유지)
        if( result == null ) return null; // 로그인 실패시 
        // 4. 토큰(token) **2개** 발급 요청
        String accessToken = jwtUtil.createAccessToken( result.getMno() );
        String refreshToken = jwtUtil.createRefreshToken( result.getMno() );
        // 5. refeshToken 만 **레디스** 에 저장
        redisTokenService.setRefreshToken( result.getMno() , refreshToken);
        // 2. 로그인 성공 시 쿠키 2개 생성/발급 , 쿠키만료기간 == 토큰만료기간 동일권장
        ResponseCookie cookie1 = ResponseCookie.from("accessToken" , accessToken)
                                .path("/").maxAge(Duration.ofMinutes(30) ) // 30분
                                .httpOnly(true).secure(false).sameSite("Lax").build();
        ResponseCookie cookie2 = ResponseCookie.from("refreshToken" , refreshToken)
                        .path("/").maxAge(Duration.ofDays(7) ) // 7일 
                        .httpOnly(true).secure(false).sameSite("Lax").build();

        // 3. 응답 헤더에 쿠키 2개 등록 , response.setHeader( )
        response.addHeader( HttpHeaders.SET_COOKIE  , cookie1.toString() );
        response.addHeader( HttpHeaders.SET_COOKIE  , cookie2.toString() );
        return result;
    }

    // [3] 내정보조회
    @GetMapping("/me")
    public MemberDto getMyInfo( @CookieValue (value="accessToken" , required = false ) String token ){
        //1. 만약에 token 가 없다면 비로그인
        if( token == null ) return  null;
        // ********* 쿠키에 저장된 token 이용하여 회원번호 찾기 ************
        Long loginMno = jwtUtil.getMnoFromToken(token);
        // 2. 로그인 중이면 서비스에게 회원정보 요청
        return memberService.getMyInfo( loginMno );
    }

    // [4] 로그아웃 + 쿠키 
    @PostMapping ("/logout")
    public boolean logout( @CookieValue(value = "accessToken" , required = false ) String accessToken, HttpServletResponse response ){
            // 1. 만약에 accessToken 존재하면 회원번호 조회 
            if( accessToken != null ){
                Long mno = jwtUtil.getMnoFromToken(accessToken); 
                redisTokenService.deleteRefreshToken(mno); // 2. 만약에 회원번호 조회 되면 레디스내 refresh 토큰 삭제하기.
            }
            // 3. 쿠키 2개 삭제
            ResponseCookie cookie1 = ResponseCookie.from("accessToken" , "" )
                                    .path("/").maxAge( 0 ) // 0초
                                    .httpOnly(true).secure(false).build();
            ResponseCookie cookie2 = ResponseCookie.from("refreshToken" , "" )
                                    .path("/").maxAge( 0 ) // 0초
                                    .httpOnly(true).secure(false).build();
            response.addHeader( HttpHeaders.SET_COOKIE  , cookie1.toString() );
            response.addHeader( HttpHeaders.SET_COOKIE  , cookie2.toString() );
            return true;
    }
}
