package example.day03;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    // 4. http://localhost:8080/day03/task8?name=유재석&age=10
    @DeleteMapping("/task8")
    public int task8( @RequestParam Map<String,Object> map ){
        System.out.println( map );
        return 8;
    }
    // 5.http://localhost:8080/day03/task9?name=유재석&age=10
    @DeleteMapping("/task9")
    public int task9( @ModelAttribute ExamDto examDto ){
        System.out.println( examDto );
        return 9;
    }
    // 6. http://localhost:8080/day03/task10/유재석/10
    @GetMapping("/task10/{name}/{age}")
    public int task10( 
        @PathVariable( name = "name") String name , 
        @PathVariable( name ="age") int age ){
        System.out.println( name ); 
        System.out.println( age );
        return 10;
    }
    // 7. http://localhost:8080/day03/task11 , { "name" : "유재석" , "age" : 10 }
    @PostMapping("/task11")
    public int task11( @RequestBody ExamDto examDto ){
        System.out.println( examDto );
        return 11;
    }

} // CLASS END 
/*
    요청Request ----------->  
                   HTTP
            <--------------- 응답Response
    * HTTP content-type : 전송하는 데이터의 타입 뜻
    쿼리스트링이란? URL?매개변수명=값&매개변수명=값
    경로구분자이란? URL/값1/값2
    1. @RequestParam : 쿼리스트링 또는 content-tpye:'application/x-www-form-urlencoded'(기본값)
    2. @ModelAttribute(생략가능) : + DTO 매핑
    3. @PathVariable : 경로구분자
    4. @RequestBody : content-tpye:'application/json'
    -> POST / PUT 메소드만 body(본문-경로상의값노출이없다) 지원한다. 
*/