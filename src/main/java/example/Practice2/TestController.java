package example.Practice2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 해당 클래스가 HTTP 컨트롤러 객체(빈)등록, @Contrller( +@Component ) + @ResponseBody
public class TestController {
    // DI : IOC(제어역전) 기반으로 스프링에 객체(빈)가 등록된 객체(빈) 주입
    @Autowired private TestService testService;
    
    // [1]
    @PostMapping("/test")
    public boolean testWrite( @RequestBody TestEntity testEntity ){
        return testService.testWrite( testEntity );
    }
    // [2]
    @GetMapping( "/test")
    public List<TestEntity> testPrint(){ 
        return testService.testPrint( );
    }
    // [3]
    @GetMapping( "/test/detail" )
    public TestEntity testDetail( @RequestParam( name = "no") int no ){
        return testService.testDetail( no );
    }
}
