package example.day14_;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 클라이언트가 /pub/chat/message 로 전송하면 처리되는 핸들러
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 입장 메시지 처리 로직 분기 예시
        if ("ENTER".equals(message.getType())) {
            message.setContent(message.getSender() + "님이 입장하셨습니다.");
        }

        // 해당 방(roomId)을 구독 중인 클라이언트들에게 브로드캐스팅
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}