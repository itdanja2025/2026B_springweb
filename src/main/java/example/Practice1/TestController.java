package example.Practice1;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Component // 스프링 컨테이너 빈(객체) 등록
// @Controller // HTTP 서블릿 + @Component
@RestController // @ResponseBody: http(응답) content-tpye:application/json + @Controller
// 주로 뷰 반환: @Controller , 값 반환: @RestController
@RequestMapping("/test") // 해당 클래스내 메소드들의 공통URL 정의
public class TestController {
    // [1] 게시물등록 // http://localhost:8080/test , { "content" : "안녕하세요","writer" : "유재석"}
    @PostMapping("") // 상위(클래스)에 "/test" 주소가 정의됨. 
    public boolean testWrite( @RequestBody TestDto TestDto ){   System.out.println("TestController.testWrite()");
        return true; // 임의로 반환 성공
    }
    // [2] 게시물 전체조회
    @GetMapping("") // http://localhost:8080/test
    public ArrayList<TestDto> testPrint(){  System.out.println("TestController.testPrint()");
        ArrayList<TestDto> list = new ArrayList<>();
        list.add( new TestDto( 1 , "내용1" , "작성자1") );
        list.add( new TestDto( 2 , "내용2" , "작성자2") );
        return list;
    }
    // [3] 게시물 개별조회
    @GetMapping("/detail") // http://localhost:8080/test/detail?no=1
    public TestDto testDetail( @RequestParam( name = "no") int no ){ System.out.println("TestController.testDetail()");
        return new TestDto(1, "내용1", "작성자1");
    }
    // [4] 게시물 삭제
    @DeleteMapping("/{no}") // http://localhost:8080/test/1
    public boolean testDelete( @PathVariable( name = "no") int no ){    System.out.println("TestController.testDelete()");
        return true;
    }
    // [5] 게시물 수정
    @PutMapping("") // http://localhost:8080/test , { "no" : "1", "content" : "안녕하세요2"}
    public boolean testUpdate( @RequestBody TestDto testDto ){ System.out.println("TestController.testUpdate()");
        return true;
    }

} // CLASS END

@Data // getter setter toString 등등
@AllArgsConstructor @NoArgsConstructor @Builder
class TestDto{
    private Integer no;
    private String content;
    private String writer;
} 
/*
    - int : 기본타입 +- 21억저장
    - Integer: 참조타입( int 래퍼클래스 ) + null(없다뜻) 저장
    주의할점: HTTP 파싱하는 경우에 int 에는 NULL 저장 불가능
    권장!: 기본타입 대신에 래퍼타입 사용하자.
*/
