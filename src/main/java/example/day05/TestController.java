package example.day05;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired private TestService testService;

    // 1. 전체조회
    @GetMapping("/test")
    public List<TestDto> 전체조회( ){
        return testService.전체조회( );
    }
    // 2. 등록
    @PostMapping("/test")
    public boolean 저장( @RequestBody TestDto testDto ){
        return testService.저장( testDto );
    }

    // 3. 수정 

}
