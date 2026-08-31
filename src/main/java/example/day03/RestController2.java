package example.day03;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// @Component  // 1. 스프링 컨테이너에 객체(빈) 등록
// @Controller // 2. HTTP 서블릿 지원 + @Component포함
@RestController// 3. 응답 content-tpye을 'application/json' 설정(@ResponseBody) + @Controller
// 활용: HTML(VIEW) -> @Controller , JSON(값) -> @RestController
@RequestMapping( "/day03" ) // *클래스내 메소드들의 공통URL 정의
public class RestController2 {
    // 1. 해당 클래스가 @RestController 이면 @ResponseBody 생략가능
    @GetMapping("/task5") // 중복없는 URL 정의
    public String task5(){ return "서버에서 응답하는 메시지"; }
    // ---------- 요청 매개변수 ----------- //
    // 2. @RequestParam이란? 요청 content-type이 (HTML)form 또는 쿼리스트링의 매개변수 매핑/연결
    // http://localhost:8080/day03/task6?name=유재석&age=10
    @GetMapping("/task6") // 클래스내 동일한 URL에 대해서는 @RequestMapping 에서 정의한다.
    public int task6( @RequestParam String name , @RequestParam int age ){
        System.out.println( name ); System.out.println( age );
        return 6;
    }
    // 3. http://localhost:8080/day03/task7?name=유재석&age=10&count=80
    @GetMapping( "/task7" )
    public int task7( String name ,  // @RequestParam 생략가능
        @RequestParam( name = "age" ) int age , // @RequestParam( name = "매핑할매개변수명" )
        @RequestParam( required = false , defaultValue = "10"  ) int count 
        // @RequestParam( required = "필수여부" , defaultValue = "기본값"  ) 
    ){
        System.out.println( name ); System.out.println( age ); System.out.println( count );
        return 7;
    }
} // CLASS END 
