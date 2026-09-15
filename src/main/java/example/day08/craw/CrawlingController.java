package example.day08.craw;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/craw")
public class CrawlingController {
    private final CrawlingService crawlingService;

    // [1]
    @GetMapping("/test1")
    public List<String> test1(){
        return crawlingService.test1();
    }

    // [2]
    @GetMapping("/test2")
    public List<Map<String, Object> > test2(){
        return crawlingService.test2();
    }

    // [3]
    @GetMapping("/test3")
    public Map< String, Object > test3(){
        return crawlingService.test3();
    }

    // [4]
    @GetMapping("/test4")
    public List<String> test4( ){
        return crawlingService.test4();
    }
}









