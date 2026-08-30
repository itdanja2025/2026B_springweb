package example.day03_0831;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
// REST란? HTTP GET/POST/PUT/DELETE 활용하여 통신
// Controller란? view(사용자/클라이언트) 와 model(dao) 사이의 통신(HTTP) 중계
// @Component // 스프링이 해당 클래스를 이해할 수 있게 스프링 컨테이너에 빈(객체)정보 등록 vs 싱글톤생성
@Controller// @Component + HTTP 기능까지 포함된 어노테이션( 서블릿 포함 )

    // 1] @Controller( +Component ) 이므로 싱글톤 생략
    // 2] HTTP 기능(방법/함수/메소드/행위)
    // XXXMapping : 클라이언트가 요청한 HTTP 메소드 와 매핑(연결) 어노테이션

    [ REST Controller 응답 원리 ]
    1. @Controller + @ResponseBody = @RestController
       - @Controller는 기본적으로 HTML 뷰 템플릿(View) 이름을 반환
       - @ResponseBody를 붙이면 뷰를 거치지 않고 HTTP 응답 Body(본문)에 데이터를 직접 작성

    2. HttpMessageConverter (HTTP 메시지 컨버터)
       - 자바 객체/데이터를 HTTP 응답 본문의 형식(MIME 타입)으로 자동 변환
       - String/기본타입 -> StringHttpMessageConverter (text/plain)
       - DTO/Map/List   -> MappingJackson2HttpMessageConverter (application/json)
       즉] String 제외한 자바의 대부분 타입은 application/json 으로 HTTP Content-Type 으로 설정된다.
    =========================================================================
*/

@Controller // Spring MVC 컨트롤러 빈 등록
public class RestController1 {

    // 1. 숫자(기본형) 반환 -> application/json 
    @GetMapping("/day03/task1")
    @ResponseBody
    public int method1() {
        System.out.println("RestController1.method1 실행");
        return 100;
    }

    // 2. 문자열 반환 -> text/plain
    @GetMapping("/day03/task2")
    @ResponseBody
    public String method2() {
        System.out.println("RestController1.method2 실행");
        return "유재석";
    }

    // 3. Map 타입 반환 -> application/json ({"유재석":100, "강호동":90})
    @GetMapping("/day03/task3")
    @ResponseBody
    public Map<String, Object> method3() {
        Map<String, Object> map = new HashMap<>();
        map.put("유재석", 100);
        map.put("강호동", 90);
        return map;
    }

    // 4. 불리언(기본형) 반환 -> application/json 
    @GetMapping("/day03/task4")
    @ResponseBody
    public boolean method4() {
        return true;
    }

    // 5. DTO(객체) 타입 반환 -> application/json ({"name":"유재석","point":100})
    @GetMapping("/day03/task5")
    @ResponseBody
    public ExamDto method5() {
        // Jackson 라이브러리가 DTO의 Getter를 이용해 JSON 문자열로 변환(직렬화)
        return new ExamDto("유재석", 100);
    }
}
