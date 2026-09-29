package example.day12_;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    // 1. 스프링 부트가 자동 등록해주는 문자열 전용 Redis 템플릿
    private final StringRedisTemplate redisTemplate;

    // =========================================================================
    // 1. Refresh Token 저장 (로그인 시 & 토큰 갱신 시 호출)
    // =========================================================================
    public void setRefreshToken(Long mno, String refreshToken) {
        // Redis 저장 명령: SET key value EX expiration
        // Key: "RT:1", Value: "jwt_refresh_token_문자열", TTL: 밀리초 단위
        redisTemplate.opsForValue().set(
                    "RT:" + mno,
                    refreshToken,
                    Duration.ofDays(7)
        );
    }

    // =========================================================================
    // 2. Refresh Token 조회 (재발급 검증 시 호출)
    // =========================================================================
    public String getRefreshToken(Long mno) {
        // Key에 해당하는 Value 조회 (만료되었거나 없으면 null 반환)
        return redisTemplate.opsForValue().get("RT:" + mno);
    }

    // =========================================================================
    // 3. Refresh Token 삭제 (로그아웃 시 또는 토큰 탈취 감지 시 호출)
    // =========================================================================
    public boolean deleteRefreshToken(Long mno) {
        // Key 삭제 -> 이후 재발급 요청 시 불일치하여 거부됨
        return Boolean.TRUE.equals(redisTemplate.delete("RT:" + mno));
    }

    // =========================================================================
    // 4. (부가 기능) 공통 문자열 저장/조회/삭제 메서드 (인증번호, 범용 캐시용)
    // =========================================================================
    public void setValues(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteValues(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}