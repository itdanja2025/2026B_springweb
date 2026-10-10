package example.day15_;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ApiController {

    private final ApiService apiService;

    // [1]
    @GetMapping("/chat1")
    public String chat1( @RequestParam("prompt") String prompt ){
        return apiService.chat1( prompt );
    }
    // [2]
    @GetMapping("/chat2")
    public String chat2(){
        return apiService.chat2();
    }
        // [3]
    @GetMapping("/chat3")
    public String chat3( @RequestParam("topic") String topic ){
        return apiService.chat3( topic );
    }
        // [4]
    @GetMapping("/chat4") 
    public String chat4( @RequestParam("userMessage")  String userMessage ){
        return apiService.chat4(userMessage);
    }
}













