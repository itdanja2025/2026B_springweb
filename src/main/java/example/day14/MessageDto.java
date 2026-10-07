package example.day14;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder 
public class MessageDto {
    private String type; // 메시지 형식 , TALK 메시지 / ENTER 접속
    private String roomId; // 방번호
    private String sender; // 보낸사람
    private String content; // 보낸내용
    private String date; // 보낸시간
}
