package example.day14;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationService {

    // 연결 타임아웃: 60분
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    // 사용자 ID별 SseEmitter 저장소
    private final Map<String, SseEmitter> emitterRepository = new ConcurrentHashMap<>();

    /**
     * 클라이언트 SSE 구독 처리
     */
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.put(userId, emitter);

        emitter.onCompletion(() -> {
            log.info("SSE 연결 완료: userId={}", userId);
            emitterRepository.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.warn("SSE 연결 타임아웃: userId={}", userId);
            emitter.complete();
            emitterRepository.remove(userId);
        });
        emitter.onError((e) -> {
            log.error("SSE 에러 발생: userId={}", userId, e);
            emitter.complete();
            emitterRepository.remove(userId);
        });

        // 503 방지 더미 연결 이벤트
        sendToClient(userId, emitter, "CONNECT", "SSE 연결 성공: userId=" + userId);

        return emitter;
    }

    /**
     * 특정 사용자에게 알림/이벤트 전송
     */
    public void sendNotification(String userId, String eventName, Object data) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            sendToClient(userId, emitter, eventName, data);
        } else {
            log.warn("구독 중이지 않은 사용자입니다: userId={}", userId);
        }
    }

    /**
     * ★ [추가] 전체 구독 중인 모든 사용자에게 알림/이벤트 브로드캐스트
     */
    public void sendToAllClients(String eventName, Object data) {
        log.info("전체 브로드캐스트 시작: 현재 연결 수={}, eventName={}", emitterRepository.size(), eventName);

        // 등록된 모든 Emitter 순회 발송
        emitterRepository.forEach((userId, emitter) -> {
            sendToClient(userId, emitter, eventName, data);
        });
    }

    private void sendToClient(String userId, SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
        } catch (IOException e) {
            log.error("메시지 전송 실패로 인한 Emitter 정리: userId={}", userId);
            emitter.complete();
            emitterRepository.remove(userId);
        }
    }
}