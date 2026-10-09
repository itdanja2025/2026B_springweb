// 패키지 선언: 해당 서비스 클래스가 속한 패키지 경로
package example.day14;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
// 스프링 스테레오타입 어노테이션: 해당 클래스를 스프링 컨테이너의 서비스 빈(Bean)으로 등록
import org.springframework.stereotype.Service;
// Spring MVC 어노테이션/클래스: 서버-클라이언트 간 SSE 스트리밍 연결을 관리하는 비동기 요청 처리 객체
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

// 입출력 예외 처리를 위한 표준 Java Exception 클래스
import java.io.IOException;
import java.time.LocalTime;
// 컬렉션 인터페이스: 객체 목록을 관리하기 위한 List 인터페이스
import java.util.List;
// 동시성 유틸리티 컬렉션: 멀티스레드 환경에서 안전하게 순회 및 수정(Thread-Safe)이 가능한 동시성 리스트
import java.util.concurrent.CopyOnWriteArrayList;

// 비즈니스 로직을 수행하는 서비스 컴포넌트 빈으로 스프링에 등록
@Service
@RequiredArgsConstructor 
public class NotificationService {

    // 1. 연결된 클라이언트들의 SseEmitter 세션 객체를 메모리에 보관하는 리스트
    // 여러 스레드가 동시에 접속(add), 종료(remove), 브로드캐스팅(순회)을 시도하므로 동시성 안전(Thread-Safe)한 CopyOnWriteArrayList 사용
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // 2. 신규 클라이언트 구독 처리 메서드 (새로운 SSE 연결 인스턴스 생성 및 수명 주기 설정)
    public SseEmitter subscribe() {
        // 클라이언트와 지속적인 HTTP 연결 스트림을 유지하기 위한 SseEmitter 객체 생성 (기본 타임아웃 적용)
        SseEmitter emitter = new SseEmitter();
        
        // 새로 생성된 클라이언트 에미터(세션)를 활성 세션 목록에 등록
        emitters.add(emitter);

        // 클라이언트와의 통신이 정상적으로 완료/종료되었을 때 실행되는 콜백: 리스트에서 해당 세션 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        
        // 브라우저와의 연결 유지 시간이 만료(Timeout)되었을 때 실행되는 콜백: 메모리 누수 방지를 위해 리스트에서 해당 세션 제거
        emitter.onTimeout(() -> emitters.remove(emitter));
        
        // 네트워크 끊김 등 비정상적인 전송 에러가 발생했을 때 실행되는 콜백: 리스트에서 해당 에미터 제거
        emitter.onError((e) -> emitters.remove(emitter));
        
        // 생성 및 이벤트 리스너가 바인딩된 SseEmitter 객체를 컨트롤러로 반환하여 HTTP 응답 스트림 수립
        return emitter;
    }

    // 3. 현재 연결 중인 전체 클라이언트를 대상으로 실시간 알림 데이터 푸시 (Broadcast)
    public void broadcast(String message) {
        // 활성화된 모든 클라이언트 SseEmitter 객체를 순차적으로 순회
        for (SseEmitter emitter : emitters) {
            try {
                // SseEmitter 전용 이벤트 빌더를 사용해 이벤트 이름을 "notice"로 지정하고 전달할 메시지 데이터를 패킷화하여 전송
                emitter.send( SseEmitter.event().name("notice").data(message) );
            } catch (IOException e) {
                // 클라이언트 브라우저가 강제 종료되는 등 소켓 연결이 끊어져 전송 실패(IOException)가 발생한 경우
                // 더 이상 유효하지 않은 좀비 세션이므로 목록에서 즉시 제거
                emitters.remove(emitter);
            }
        }
    }
    // -------------------------------------------------------------
    // [추가] 4. 10초(1000ms)마다 접속 중인 전체 클라이언트에 자동 SSE 메시지 푸시
    // -------------------------------------------------------------
    @Scheduled(fixedRate = 10000)
    public void autoSendSseNotice() {
        // 접속 중인 클라이언트가 없으면 실행 생략
        if (emitters.isEmpty()) return;

        // 1초마다 보낼 메시지 생성 (현재 시간 포맷팅)
        String timeMessage = "[SSE 자동 공지] 현재 서버 시간: " + LocalTime.now().withNano(0);
        
        // 기존 broadcast 메서드를 호출하여 전송
        broadcast(timeMessage);
    }

    private final SimpMessageSendingOperations messageTemp;
        // -------------------------------------------------------------
    // [추가] 3. 10초(1000ms)마다 특정 방(general)으로 자동 STOMP 메시지 발행
    // -------------------------------------------------------------
    @Scheduled(fixedRate = 10000)
    public void autoSendStompMessage() {
        // 1. 자동 전송용 MessageDto 객체 빌드/생성
        MessageDto autoDto = new MessageDto();
        autoDto.setType("TALK");
        autoDto.setRoomId("3"); // 자동 메시지를 보낼 대상 방 ID
        autoDto.setSender("SYSTEM_BOT");
        autoDto.setContent("1초 주기 자동 브로드캐스팅 메시지");
        autoDto.setDate(LocalTime.now().withNano(0).toString());

        // 2. 해당 방을 구독 중인 클라이언트들에게 convertAndSend로 발행
        // /sub/chat/room/general 토픽을 구독 중인 사용자 전원에게 전송됨
        messageTemp.convertAndSend("/sub/chat/room/" + autoDto.getRoomId(), autoDto);
    }


}