package example.day10;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/member")
@RequiredArgsConstructor 
public class MemberController {
    private final MemberService memberService;
    // [1] 회원가입
    @PostMapping("/signup")
    public boolean signup( @RequestBody MemberDto memberDto ){
        return memberService.signup( memberDto );
    }
}
/*

    @GetMapping("")
    public String test( HttpServletRequest request ){
        //1) HttpServletRequest: HTTP 요청이 들어오면 요청 정보가 담겨 있는 객체
        System.out.println( request.getRemoteAddr() ); // 요청한 클라이언트의 IP
        System.out.println( request.getHeader("User-Agent")); // 요청한 클라이언트 브라우저 정보
        System.out.println( request.getSession() ); // 요청한 클라이언트의 세션객체 정보
        // 2) 새션객체란? 톰캣 서버내 브라우저 마다 독립적인 저장소
        // 주로 : *로그인성공정보*, 인증번호, 비회원제장바구니 등등 일시적인 휘발성 메모리
        HttpSession session = request.getSession(); // 세션객체내 여러개 정보 저장 가능
        System.out.println( session.getId() ); // 세션 식별번호 
        System.out.println( session.getCreationTime() ); // 세션 생성시간
        System.out.println( session.getLastAccessedTime() ); // 세션 마지막접근 시간
        System.out.println( session.getMaxInactiveInterval() ); // 세션 생명주기( 기본값30분 )
        // 3) 세션 정보 저장=로그인/호출=마이페이지/삭제=로그아웃
        session.setAttribute( "data", "사과"); // map(key,value)구조
        // data 이름(key) 으로 사과(data) 저장 , 주의할점: value 타입은 Object 이라서 타입변환 필요
        System.out.println( session.getAttribute("data")); // key 이용한 value 호출 
        session.invalidate(); // 세션 초기화
        return session.getId();
    }

*/