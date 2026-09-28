package example.day10;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/member")
@RequiredArgsConstructor 
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {
    private final MemberService memberService;
    // [1] 회원가입
    @PostMapping("/signup")
    public boolean signup( @RequestBody MemberDto memberDto ){
        return memberService.signup( memberDto );
    }

    // [2] 로그인 + 세션(인증 성공시 성공한 회원정보 저장/왜? 로그인 성공한 회원이 글쓰기/제품등록 등등 FK용도)
    @PostMapping("/login")
    public MemberDto login( @RequestBody MemberDto memberDto , HttpSession session ){
        // 1. 서비스에게 인증 확인 한다.
        MemberDto result = memberService.login(memberDto);
        if( result == null ) return  null; // 로그인실패
        // 2. 인증 성공이면 세션에 인증한 회원정보 담아주기.
        // - 매개변수에 HttpSession 객체 정의
        // - 'login_member' key(이름) 으로 memberDto value(로그인성공한) 정보 저장
        session.setAttribute("login_member", result ); // Object (자동) 업캐스팅, dto->obj
        return result;
    }

    // [3] 내정보조회 + 세션 ( 이미 로그인된 회원이 내정보 요청 ) 
    @GetMapping("/me")
    public MemberDto getMyInfo( HttpSession session ){
        // * 사용자에게 추가로 입력받을 값은 없다.
        // 1) 세션에서 특정한(login_member) 정보 꺼내기
        Object obj = session.getAttribute("login_member");
        if( obj == null ) return null; // 세션 정보가 비어 있으면 실패
        // 2) 존재하면 Object 다운캐스팅, obj -> dto
        MemberDto memberDto = (MemberDto)obj;
        // 3) 서비스에게 회원번호 전달하여 추가 정보 요청하여 반환한다.
        return memberService.getMyInfo( memberDto.getMno() );
    }

    // [4] 로그아웃 + 세션 ( 초기화 )
    @PostMapping ("/logout")
    public boolean logout( HttpSession httpSession ){
        //* 사용자에게 추가로 입력받을 값은 없다.
        httpSession.invalidate(); // 선택1] 세션 내 모든 정보 초기화
        // httpSession.removeAttribute("login_member"); // 선택2] 세션 내 특정 정보 삭제
        return true;
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