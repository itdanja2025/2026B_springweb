package example.day15;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController@RequiredArgsConstructor 
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class LLMController {
    private final LLMService llmService;
    // 1. http://localhost:8080/api/chat1?promt=안녕
    @GetMapping("/chat1")
    public String chat1( 
        @RequestParam("prompt") String prompt ){
        return llmService.chat1(prompt);
    }
    // 2. 
    @GetMapping("/chat2")
    public String chat2( ){
        return llmService.chat2();
    }
    // 3. 
    @GetMapping("/chat3")
    public String chat3( 
        @RequestParam("topic") String topic ){
            return llmService.chat3(topic);
    }
    // 4.
    @GetMapping ("/chat4")
    public String chat4( @RequestParam("userMessage") String msg ){
            return llmService.chat4(msg);
    }
}
