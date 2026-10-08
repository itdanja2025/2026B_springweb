package example.day14;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class MessageController {
    // 1. 메시지 템플릿  
    private final SimpMessageSendingOperations messageTemp;
    // 2. 메시지 매핑 메소드 , @MessageMapping("/주소")
    @MessageMapping("/chat/message")
    public void message( MessageDto messageDto ){
        // 2-1 : ws://localhost:8080/ws-chat/pub/chat/message 요청시 실행되는 메소드
        // 2-2 : 내용물(body) 들을 MessageDto 매핑한다.
        // [생략] 만약에 메시지 내용 영구저장 --> DB(JPA)
        // 2-4 : 입장메시지 , 퇴장메시지 구분
        if( messageDto.getType().equals("ENTER") ){
            messageDto.setContent( messageDto.getSender()+"님이 입장"); // 입장 메시지
        }else if( messageDto.getType().equals("QUIT") ){
            messageDto.setContent( messageDto.getSender()+"님이 퇴장"); // 퇴장 메시지 
        }
        // 2-3 : 같은 방을 구독하는 클라이언트에게 메시지 전송 
        // messageTemp.convertAndSend( "/보낼주소" , 내용물 );
        // 보낼주소 : ws://localhost:8080/ws-chat/sub/chat/room/4
        // [생략] 만약에 데이터베이스내 메시지 보낸사람과 같은 방 조회 --> DB(JPA)
        messageTemp.convertAndSend( "/sub/chat/room/"+messageDto.getRoomId() , messageDto );
    }
}
