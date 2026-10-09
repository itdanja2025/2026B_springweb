// 패키지 선언: 해당 컨트롤러 클래스가 위치한 패키지 경로
package example.day14;

// Lombok 어노테이션: final 또는 @NonNull 필드를 매개변수로 갖는 생성자를 자동 생성
import lombok.RequiredArgsConstructor;
// Spring HTTP 유틸리티: 표준 미디어 타입(MIME Type, 예: text/event-stream) 상수 제공
import org.springframework.http.MediaType;
// Spring Web 어노테이션: REST API 컨트롤러 및 요청 매핑(@GetMapping, @RequestParam 등) 지원
import org.springframework.web.bind.annotation.*;
// Spring MVC 클래스: 비동기 HTTP 연결을 유지하며 서버에서 클라이언트로 이벤트를 스트리밍하는 객체
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

// JSON 또는 스트림 형태의 데이터를 반환하는 REST 컨트롤러 빈으로 등록
@RestController
// 해당 컨트롤러의 공통 URL 기본 경로를 '/api/sse'로 지정
@RequestMapping("/api/sse")
// final로 선언된 notificationService 필드의 생성자 주입(DI) 코드 자동 생성
@RequiredArgsConstructor 
// 다른 출처(도메인/포트)에서의 웹 요청을 허용하여 CORS 문제 방지
@CrossOrigin(origins = "*") // CORS 허용
public class NotificationController {

    // 실시간 알림 비즈니스 로직(세션 등록 및 이벤트 발송)을 처리하는 서비스 객체 의존성 주입
    private final NotificationService notificationService;

    // 1. 클라이언트 SSE 구독 요청 처리 엔드포인트
    // produces 속성을 통해 응답 헤더의 Content-Type을 'text/event-stream'으로 설정하여 브라우저에 스트림 연결임을 명시
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        // 서비스 계층에서 생성 및 관리되는 SseEmitter 객체를 반환하여 클라이언트와 지속적인 단방향 HTTP 연결 수립
        return notificationService.subscribe();
    }

    // 2. 접속 중인 전체 클라이언트를 대상으로 알림 메시지를 일괄 발송하는 테스트용 GET 엔드포인트
    // HTTP 요청 쿼리 파라미터(?message=내용)를 message 변수로 바인딩
    @GetMapping("/broadcast")
    public String broadcast(@RequestParam(name = "message") String message) {
        // 서비스의 브로드캐스트 로직을 호출하여 현재 구독 중인 모든 클라이언트에게 메시지 전송
        notificationService.broadcast(message);
        // 발송 결과를 확인하기 위한 단순 안내 문자열 반환
        return "전체 알림 발송 완료: " + message;
    }
}