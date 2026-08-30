package example.day03_0831;

import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/*
    =========================================================================
    [ URI 구조 구분: '/' vs '?' ]
    1. 경로 구분자 ( / ) : 리소스의 위치/계층 식별 (@PathVariable)
       - 형식: http://localhost:8080/day02/task13/100
    2. 쿼리스트링 시작 ( ? ) : 데이터 필터링, 정렬, 옵션 전달 (@RequestParam, @ModelAttribute)
       - 형식: http://localhost:8080/day02/task7?name=유재석&age=40 (구분자: ?, 파라미터 연결: &)

    [ 직렬화(Serialization) vs 역직렬화(Deserialization) ]
    1. 역직렬화 (Deserialization): 외부 데이터(JSON 텍스트, Binary) -> Java 객체로 변환
       - @RequestBody: HTTP 요청 본문의 JSON 텍스트를 Jackson 라이브러리(ObjectMapper)가 Java DTO/Map 객체로 파싱
       - 동작 조건: DTO에 기본 생성자(NoArgsConstructor) 및 Getter 또는 Setter 필요
    2. 직렬화 (Serialization): Java 객체 -> 외부 포맷(JSON 텍스트, Byte Stream)으로 변환
       - @ResponseBody (@RestController에 내장): Java 반환 객체(DTO, Map, List 등)를 JSON 텍스트로 변환하여 HTTP 응답 본문에 작성
       - 동작 조건: DTO에 Getter 필수 (Jackson이 Getter 메서드를 기반으로 JSON 프로퍼티 생성)

    [ Content-Type 및 바인딩 요약 ]
    1. application/x-www-form-urlencoded (쿼리스트링 / Form 전송)
       - @RequestParam   : 1:1 파라미터 개별 바인딩 (문자열 -> 기본 타입 변환기 적용)
       - @ModelAttribute : DTO 객체 일괄 바인딩 (Setter/생성자 기반 주입, 직렬화 과정 없음)
    2. application/json (REST API 본문 전송)
       - @RequestBody    : HttpMessageConverter(Jackson)를 통한 JSON 역직렬화
    3. multipart/form-data (파일 업로드)
       - MultipartFile(바이너리) + @ModelAttribute(텍스트 필드)
    =========================================================================
*/
@RestController
@RequestMapping("/day02")
public class RestController2 {

    // -------------------------------------------------------------
    // 1. 단순 문자열 응답 (경로만 존재: /)
    // GET: http://localhost:8080/day02/task6
    // -------------------------------------------------------------
    @GetMapping("/task6")
    public String method1() {
        return "서버에게 받은 메시지";
    }

    // -------------------------------------------------------------
    // 2. @RequestParam 기본 사용 (쿼리스트링: ?)
    // GET: http://localhost:8080/day02/task7?name=유재석&age=40
    // -------------------------------------------------------------
    @GetMapping("/task7")
    public int method2(@RequestParam String name, @RequestParam int age) {
        System.out.println("RestController2.method2 -> name = " + name + ", age = " + age);
        return 7;
    }

    // -------------------------------------------------------------
    // 3. @RequestParam 옵션 종합 (어노테이션 생략, 필수 해제, 이름 매핑, 기본값) (쿼리스트링: ?)
    // GET: http://localhost:8080/day02/task8?userAge=40
    // -------------------------------------------------------------
    @GetMapping("/task8")
    public int method3(
            String name,                                        // 생략 가능 (생략 시 required = false)
            @RequestParam(required = false) String address,     // 필수 아님 (누락 시 null)
            @RequestParam(name = "userAge") int age,            // 쿼리 파라미터명(userAge)과 변수명(age) 매핑
            @RequestParam(defaultValue = "1") int count         // 누락 시 기본값 적용
    ) {
        System.out.println("RestController2.method3 -> name = " + name + ", address = " + address + ", age = " + age + ", count = " + count);
        return 8;
    }

    // -------------------------------------------------------------
    // 4. 여러 쿼리 파라미터를 Map으로 일괄 수신 (쿼리스트링: ?)
    // DELETE: http://localhost:8080/day02/task9?name=유재석&age=40
    // -------------------------------------------------------------
    @DeleteMapping("/task9")
    public int method4(@RequestParam Map<String, Object> map) {
        System.out.println("RestController2.method4 -> map = " + map);
        return 9;
    }

    // -------------------------------------------------------------
    // 5. @ModelAttribute: DTO 일괄 바인딩 (쿼리스트링: ? 또는 Body Form Data)
    // [CASE 1] GET 요청 쿼리스트링
    //   GET: http://localhost:8080/day02/task10?name=유재석&age=40
    // [CASE 2] POST 요청 Body 전송
    //   POST: http://localhost:8080/day02/task10
    //   Header: Content-Type: application/x-www-form-urlencoded
    //   Body: name=유재석&age=40
    // -------------------------------------------------------------
    @PostMapping("/task10")
    public int method5(@ModelAttribute ExamDto examDto) {
        System.out.println("RestController2.method5 -> examDto = " + examDto);
        return 10;
    }

    // -------------------------------------------------------------
    // 6. @RequestBody: JSON 문자열 -> DTO 역직렬화 (URL에 ? 없이 Body로 전송)
    // POST: http://localhost:8080/day02/task11
    // Header: Content-Type: application/json
    // Body: { "name": "유재석", "age": 40 }
    // -------------------------------------------------------------
    @PostMapping("/task11")
    public int method6(@RequestBody ExamDto examDto) {
        System.out.println("RestController2.method6 -> examDto = " + examDto);
        return 11;
    }

    // -------------------------------------------------------------
    // 7. @RequestBody: JSON 문자열 -> Map 역직렬화 (URL에 ? 없이 Body로 전송)
    // PUT: http://localhost:8080/day02/task12
    // Header: Content-Type: application/json
    // Body: { "name": "유재석", "age": 40 }
    // -------------------------------------------------------------
    @PutMapping("/task12")
    public int method7(@RequestBody Map<String, Object> map) {
        System.out.println("RestController2.method7 -> map = " + map);
        return 12;
    }

    // -------------------------------------------------------------
    // 8. @PathVariable: URI 경로 변수 추출 (경로 구분자 / 사용)
    // GET: http://localhost:8080/day02/task13/100
    // (URI 경로의 /100 값을 int id로 추출)
    // -------------------------------------------------------------
    @GetMapping("/task13/{id}")
    public int method8(@PathVariable int id) {
        System.out.println("RestController2.method8 -> id = " + id);
        return id;
    }

    // -------------------------------------------------------------
    // 9. 파일 업로드 + 폼 데이터 (URL에 ? 없이 Body로 멀티파트 전송)
    // POST: http://localhost:8080/day02/task14
    // Header: Content-Type: multipart/form-data
    // Body: file=[바이너리], name=유재석, age=40
    // -------------------------------------------------------------
    @PostMapping(value = "/task14")
    public int method9(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ExamDto examDto
    ) {
        System.out.println("RestController2.method9 -> file = " + file.getOriginalFilename());
        System.out.println("RestController2.method9 -> examDto = " + examDto);
        return 14;
    }
}
