package example.day16_;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CrawlingController {

    private final CrawlingService crawlingService;

    // [1] 뉴스 기사 제목 목록
    @GetMapping("/craw1")
    public List<Map<String, Object>> craw1() {
        return crawlingService.craw1();
    }

    // [2] 베스트셀러 도서 목록
    @GetMapping("/craw2")
    public List<Map<String, Object>> craw2() {
        return crawlingService.craw2();
    }

    // [3] 날씨 및 미세먼지 정보
    @GetMapping("/craw3")
    public List<Map<String, Object>> craw3() {
        return crawlingService.craw3();
    }

    // [4] CGV 관람평 목록
    @GetMapping("/craw4")
    public List<Map<String, Object>> craw4() {
        return crawlingService.craw4();
    }
}