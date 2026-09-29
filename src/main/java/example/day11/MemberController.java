package example.day11;

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

    // [2] 로그인 + 쿠키변경( 회원 식별(번호) 쿠키에 담아 클라이언트에 전송 )
    @PostMapping("/login")
    public MemberDto login( @RequestBody MemberDto memberDto , HttpServletResponse response ){
        // 1. 서비스 에게 인증/로그인 확인 (기존 유지)
        MemberDto result = memberService.login(memberDto);
        if( result == null ) return null; // 로그인 실패시 
        // 2. 로그인 성공 시 쿠키 생성/발급 *********** 쿠키 값을 jwt 안전하게 변경 *************
        // 4. 토큰(token) 발급 요청
        String token = jwtUtil.createToken( result.getMno() ); // mno --> jwt 
        ResponseCookie cookie = ResponseCookie.from( "login_member" , token )
                                .path("/") // 쿠키 사용할 경로 , "/" 도메인내 전체
                                .maxAge( Duration.ofDays(1) ) // 쿠키의 유효기간 , 1일  // Duration.ofXXX( 수 )
                                .httpOnly(true) // JS이용한 탈취 방지 , XSS공격
                                .secure(false) // HTPPS 에서만 사용 , 개발단계:FALSE , 배포단계:TRUE 
                                .sameSite("Lax") // CSRF 공격방어
                                .build(); // 쿠키생성 끝 
        // 3. 응답 헤더에 쿠키 등록 , response.setHeader( )
        response.setHeader( HttpHeaders.SET_COOKIE  , cookie.toString() );
        return result;
    }

    // [3] 내정보조회 + 쿠키
    @GetMapping("/me")
    public MemberDto getMyInfo( 
        // @CookieValue( value="쿠키명") ){ // 요청한 브라우저의 쿠키 가져오기 
        @CookieValue (value="login_member" , required = false ) String token ){
        //1. 만약에 token 가 없다면 비로그인
        if( token == null ) return  null;
        // ********* 쿠키에 저장된 token 이용하여 회원번호 찾기 ************
        Long loginMno = jwtUtil.getMnoFromToken(token);
        // 2. 로그인 중이면 서비스에게 회원정보 요청
        return memberService.getMyInfo( loginMno );
    }

    // [4] 로그아웃 + 쿠키 
    @PostMapping ("/logout")
    public boolean logout( HttpServletResponse response ){
        // 1. 삭제할 쿠키명과 동일한 이름으로 maxAge(0) 하여 재발급
        ResponseCookie cookie = ResponseCookie.from( "login_member", "")
                            .path("/") // 모든곳에서 로그아웃 가능하도록 , 전체 
                            .maxAge(0) // 바로 삭제
                            .httpOnly(true).secure(false)
                            .build();
        // 2.응답객체내 헤더에 쿠키 포함
        response.setHeader( HttpHeaders.SET_COOKIE, cookie.toString() );
        return true;
    }
}
