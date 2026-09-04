package example.day05;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired private TestService testService;

    // 1. 전체조회 , http://localhost:8080/test
    @GetMapping("/test")
    public List<TestDto> 전체조회( ){
        return testService.전체조회( );
    }
    // 2. 등록 , http://localhost:8080/test , 
    // { "name" : "우유" , "price" : 700 , "descri" : "신선한" }
    // createDate/updateDate 자동으로 들어가는 확인!!
    @PostMapping("/test")
    public boolean 저장( @RequestBody TestDto testDto ){
        return testService.저장( testDto );
    }
    // 3. 수정 , http://localhost:8080/test , 
    // { "no" : 1,  "price" : 2000 , "descri" : "탄산음료" }
    // updateDate 자동으로 반영/수정 되는지 확인!!
    @PutMapping("/test")
    public boolean 수정( 
        @RequestBody TestDto testDto ){
        return testService.수정( testDto );
    }

}
