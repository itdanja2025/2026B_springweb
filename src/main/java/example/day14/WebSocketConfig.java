package example.day14;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// [1] WebSocket 설치(https://start.spring.io) : implementation 'org.springframework.boot:spring-boot-starter-websocket'

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 구독 요청 prefix: 클라이언트가 /sub/chat/room1 형태로 구독
        registry.enableSimpleBroker("/sub");
        
        // 메시지 발행 요청 prefix: 클라이언트가 /pub/chat/message 형태로 전송
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 핸드셰이크를 위한 HTTP 엔드포인트 등록
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*"); // 필요에 따라 특정 Origin으로 제한
    }
}
/*
    프로토콜 : 통신 약속/규칙 , 예] 신호등
    HTTP(프로토콜) : 클라이언트가 서버에게 *요청하면* 서버에게 응답받는 구조 ( *응답은 항상 요청자 에게만 한다* )
        -> 단방향 구조, 무상태(상태/값 유지 안함 ), Request/Response
        -> RESTAPI CRUD

    WebSocket(프로토콜) : 클라이언트와 서버가 연결 상태 유지하고 , 서로 통신 하는 구조
        -> 양방향 구조, 상태유지 , STOMP( pub(발행) / sub(구독) )
        -> 실시간 통신( 채팅 , 알림 , 지도/실시간위치 등등 )
*/