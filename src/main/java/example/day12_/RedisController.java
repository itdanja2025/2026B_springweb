package example.day12_;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    // [*] 간단한 텍스트를 레디스에 접근하는 객체
    private final StringRedisTemplate redisTemplate; // 템플릿이란? 미리 만들어진 틀/형식
    private final ObjectMapper objectMapper = new ObjectMapper();

    // [1] 간단한 텍스트를 레디스 서버에 저장 / 호출 하기
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        System.out.println("RedisController.test");
        // [저장] 레디스템플릿객체명.opsForValue().set( key , value ); , key 값은 중복이 안되므로 중복이면 덮여쓰기 적용
        // { "유재석" : "90"  } , { "강호동" : "80" }
        redisTemplate.opsForValue().set( "유재석" , "90" ); // 임의 데이터1
        redisTemplate.opsForValue().set( "강호동" , "80" ); // 임의 데이터2
        redisTemplate.opsForValue().set( "유재석" , "100" ); // key는 중복을 허용하지 않고 , value 중복 허용
        // [ 모든 키 호출] 레디스템플릿객체명.keys("*")         : 레디스에 저장된 모든 키 반환
        // [ 특정한 키의 값 호출] 레디스템플릿객체명.opsForValue().get( key );
        Set< String > keys = redisTemplate.keys("*");
            //  List vs Map vs Set 컬렉션 프레임워크이란?차이?
        List< Object > result = new ArrayList<>(); // 임의의 리스트
        for( String key : keys ){
            result.add( redisTemplate.opsForValue().get( key ) );
        }
        return ResponseEntity.ok( result );
    } // method end

    // [1] 등록
    // POST http://localhost:8080/redis
    // Body: { "sno": 1, "name": "유재석", "kor": 90, "math": 70 }
    @PostMapping("")
    public String save(@RequestBody MemberDto memberDto) throws JsonProcessingException {
        // 0. 중복 없는 key 구상 (예: student:1)
        String key = "member:" + memberDto.getMno();

        // 1. DTO 객체 -> JSON 문자열 변환 (직렬화)
        String jsonStr = objectMapper.writeValueAsString(memberDto);

        // 2. Redis에 순수 문자열로 저장
        redisTemplate.opsForValue().set(key, jsonStr);

        return "[저장성공]";
    }

    // [2] 전체 조회
    // GET http://localhost:8080/redis
    @GetMapping("")
    public List<MemberDto> findAll() throws JsonProcessingException {
        // 0. "student:*" 패턴의 모든 Key 조회
        Set<String> keys = redisTemplate.keys("member:*");
        List<MemberDto> result = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                // 1. Redis에서 JSON 문자열 꺼내기
                String jsonStr = redisTemplate.opsForValue().get(key);
                if (jsonStr != null) {
                    // 2. JSON 문자열 -> StudentDto 객체 변환 (역직렬화)
                    MemberDto memberDto = objectMapper.readValue(jsonStr, MemberDto.class);
                    result.add(memberDto);
                }
            }
        }

        return result;
    }

    // [3] 개별 학생 조회
    // GET http://localhost:8080/redis/find?sno=1
    @GetMapping("/find")
    public MemberDto find(@RequestParam(name="mno") int mno) throws JsonProcessingException {
        String key = "member:" + mno;
        // 1. 특정한 key의 JSON 문자열 호출
        String jsonStr = redisTemplate.opsForValue().get(key);
        if (jsonStr == null) {
            return null;
        }
        // 2. JSON 문자열 -> StudentDto 객체 변환
        MemberDto memberDto = objectMapper.readValue(jsonStr, MemberDto.class);
        return memberDto;
    }

    // [4] 개별 삭제
    // DELETE http://localhost:8080/redis?sno=1
    @DeleteMapping("")
    public boolean delete(@RequestParam(name="mno") int mno) {
        String key = "member:" + mno;
        // 1. 특정한 key 삭제 (성공 시 true, 실패 또는 없으면 false)
        Boolean result = redisTemplate.delete(key);
        return result;
    }

    // [5] 개별 수정
    // PUT http://localhost:8080/redis
    // Body: { "sno": 1, "name": "유재석", "kor": 100, "math": 100 }
    @PutMapping("")
    public boolean update(@RequestBody MemberDto memberDto) throws JsonProcessingException  {
        String key = "member:" + memberDto.getMno();
        // 1. 수정한 DTO 객체 -> JSON 문자열 변환
        String jsonStr = objectMapper.writeValueAsString(memberDto);
        // 2. 동일한 key에 덮어쓰기(Overwrite)
        redisTemplate.opsForValue().set(key, jsonStr);
        return true;
    }

    // * 인증코드 발급 해서 레디스 유효기간 정하기
    // TTL : 레디스에 저장된 엔트리(key-value) 을 특정한 기간(시간)이 되면 자동 삭제
    @GetMapping("/auth/send") // http://localhost:8080/redis/auth/send?phone=01039132072
    public ResponseEntity<?> authSend( @RequestParam(name = "phone") String phone ){
        // 1. key 구상 , "auth:고객전화번호"
        String key = "auth:"+phone;
        // 난수 6자리( 인증코드 생성 )
        String code = String.format( "%06d" , new Random().nextInt(999999) );
        // 2. 레디스에 인증코드 저장하기 , TTL(유효기간) , Duration.ofXXXX( 수 )
        redisTemplate.opsForValue().set( key , code , Duration.ofSeconds(10) ); // 10초
        // API 이용하여 고객전화번호 에게 인증코드 전송
        return ResponseEntity.ok().body("인증코드 발급 완료 : " + code );
    }
    @GetMapping("/auth/confirm") // http://localhost:8080/redis/auth/confirm?phone=01039132072&code=361170
    public ResponseEntity<?> authConfirm( @RequestParam(name = "phone") String phone , @RequestParam(name = "code") String code ){
        String key = "auth:"+phone; // 1. 조회할 key 구상
        Object savedCode = redisTemplate.opsForValue().get(key); // 2. 조회할 key 이용한 value 호출
        if( savedCode == null ){ return ResponseEntity.ok("[인증실패] 인증 만료 또는 코드 불일치 "); }
        else if( savedCode.equals( code ) ){
            redisTemplate.delete( key );            // 성공시에는 인증코드 삭제
            return ResponseEntity.ok("[인증성공]");
        }
        else{ return ResponseEntity.ok("[인증실패]"); }
    }




} // class end














