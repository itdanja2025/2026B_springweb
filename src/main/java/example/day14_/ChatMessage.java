package example.day14_;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String type;    // TALK , ENTER
    private String roomId;
    private String sender;
    private String content;
}