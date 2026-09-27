package example.day10;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {

    private final MemberService memberService;

    
    // [1] 세션객체 내 값 저장 = 로그인
    @GetMapping ("") // http://localhost:8080/api/seesion?data=세션테스트
    public String test1(@RequestParam String data , HttpServletRequest request ){
        // 1) HttpServletRequest request : HTTP 요청 정보를 담고 있는 객체
        System.out.println( request.getRemoteAddr() ); // 요청한 클라이언트(사용자) IP 주소 얻기( 로그/위치추적/조회수 등 )
        System.out.println( request.getHeader("User-Agent") ); // 요청한 클라이언트 브라우저 정보
        System.out.println( request.getSession() ); // 요청한 클라이언트의 세션객체 정보 * 각 브라우저 마다 할당 *
        // 2) 세션 객체
        HttpSession session = request.getSession();
        System.out.println( session.getId() ); // 세션의 식별번호 반환 , 브라우저/드바이스 마다 할당
        System.out.println( session.getCreationTime() ); // 세션의 최초생성 시간
        System.out.println( session.getLastAccessedTime() ); // 세션의 마지막접근 시간
        System.out.println( session.getMaxInactiveInterval() ); // 세션의 최대 유지시간(초)
        // 3) 세션 객체 내 값 저장 == 로그인
        session.setAttribute( "data" , data ); // 세션객체내 값(key:value) 저장
        System.out.println( session.getAttribute("data") ); // 세션객체내 값(key) 호출
        return session.getId();
    }



    // 1. 회원가입 (성공 시 true, 실패 시 false 반환)
    @PostMapping("/signup")
    public boolean signup(@RequestBody MemberDto memberDto) {
        Long resultMno = memberService.signup(memberDto);
        return resultMno != null;
    }

    // 2. 로그인 (성공 시 MemberDto 반환, 실패 시 null 반환)
    @PostMapping("/login")
    public MemberDto login(@RequestBody MemberDto memberDto, HttpSession session) {
        MemberDto loginUser = memberService.login(memberDto);
        
        // 로그인 실패 시 null 반환
        if (loginUser == null) {
            return null;
        }

        // 세션에 로그인 회원 정보 저장
        session.setAttribute("login_member", loginUser);
        return loginUser;
    }

    // 3. 내 정보 조회 (로그인 상태면 MemberDto 반환, 비로그인이면 null 반환)
    @GetMapping("/me")
    public MemberDto getMyInfo(HttpSession session) {
        // 1) 세션에서 회원 데이터 꺼내기 (세션이 비어있으면 null 반환)
        Object obj = session.getAttribute("login_member");
        if (obj == null) {
            return null;
        }

        // 2) MemberDto로 다운캐스팅
        MemberDto sessionUser = (MemberDto) obj;

        // 3) DB에서 최신 회원 정보 조회 후 반환
        return memberService.getMyInfo(sessionUser.getMno());
    }

    // 4. 로그아웃 (성공 시 true 반환)
    @PostMapping("/logout")
    public boolean logout(HttpSession session) {
        // 현재 세션 즉시 파기 (무효화)
        session.invalidate();
        return true;
    }
}