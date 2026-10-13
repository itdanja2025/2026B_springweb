package example.day16;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api")
public class CrawController {
    private final CrawService crawService;
    // 1. 
    @GetMapping("/craw1")
    public List<String>  craw1() {
        return crawService.craw1();
    }
    // 2. 
    @GetMapping("/craw2")
    public List<Map<String,Object>> craw2() {
        return crawService.craw2();
    }
    
} // c end 
