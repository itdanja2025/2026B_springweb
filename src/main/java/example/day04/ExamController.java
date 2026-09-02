package example.day04;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // final 멤버변수 생성자 자동
public class ExamController {
    // * 서비스객체 (주입)불러오기
    private final ExamService examService;
    // [1] 전체조회 http://localhost:8080/day04/exam
    @GetMapping("/day04/exam")
    public List<ExamEntity> 전체조회( ){
        // 서비스 호출
        return examService.전체조회();
    }
    // [2] 저장 , http://localhost:8080/day04/exam , { "ename" : "김현수"}
    @PostMapping( "/day04/exam")
    public boolean 저장( @RequestBody ExamEntity entity ){
        // 서비스 호출
        return examService.저장( entity );
    }
    // [3] 삭제 , http://localhost:8080/day04/exam?no=3
    @DeleteMapping("/day04/exam")
    public boolean 삭제( @RequestParam( name = "no") int no ){
        // 서비스호출
        return examService.삭제( no );
    }
    // [4] 수정 , http://localhost:8080/day04/exam , { "eno" : "1" , "ename" : "김현수"}
    @PutMapping( "/day04/exam" )
    public boolean 수정( @RequestBody ExamEntity entity ){
        // 서비스호출
        return examService.수정( entity );
    }

} // class end 
