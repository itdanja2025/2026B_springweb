package example.day14;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor 
@CrossOrigin(origins = "*") // React 로컬 개발 환경용 CORS 허용
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 1. SSE 구독 엔드포인트 (GET)
     * Response Header: Content-Type: text/event-stream
     */
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("userId") String userId) {
        return notificationService.subscribe(userId);
    }

    /**
     * 2. 서버 푸시 테스트용 API (POST)
     * 호출 시 특정 userId를 구독 중인 브라우저 화면에 실시간 데이터가 뜹니다.
     */
    @PostMapping("/send/{userId}")
    public String sendNotification(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, Object> payload
    ) {
        notificationService.sendNotification(userId, "alarm", payload);
        return "알림 발송 완료: " + userId;
    }

    // ★ 3. [추가] 전체 구독자 대상 브로드캐스트 발송
    @PostMapping("/broadcast")
    public String broadcastNotification(@RequestBody Map<String, Object> payload) {
        notificationService.sendToAllClients("alarm", payload);
        return "전체 알림 발송 완료";
    }
    
}