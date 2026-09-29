package example.day12_;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    // 레디스 관련 된 설정 메소드
    @Bean // 빈 등록
    public RedisTemplate<String,Object> redisTemplate(
            RedisConnectionFactory  connectionFactory ){
        // 1. 레디스 템플릿 객체 생성 : 레디스 형식을 MAP타입으로 사용하기 위한 설정
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        // 2. 생성한 템플릿 객체를 팩토리(레디스저장소) 등록
        template.setConnectionFactory( connectionFactory );
        // 3. 생성한 템플릿은 key값을 String 타입으로 직렬화 한다.
        template.setKeySerializer( new StringRedisSerializer() );
        // 4. 생성한 템플릿은 value값을 JSON/DTO 타입으로 직렬화 한다.
        template.setValueSerializer( new GenericJackson2JsonRedisSerializer() );
        // * 직렬화 : 레디스에 저장된 데이터를 자바 타입으로 변환 과정 , 역직렬화 (복원)
        return template; // 템플릿 반환
    }
}
