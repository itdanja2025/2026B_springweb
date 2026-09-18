package example.day08;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor 
public class ApiController {
    private final ApiService apiService;
    // 1. 
    @GetMapping("/test1")
    public Map<String,Object> test1(){
        return apiService.test1();
    }
    // 2.
    @GetMapping("/test2")
    public Map<String,Object> test2(){
        return apiService.test2();
    }

}
