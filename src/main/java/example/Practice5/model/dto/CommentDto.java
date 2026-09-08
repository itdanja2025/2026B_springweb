package example.Practice5.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor@AllArgsConstructor@Data@Builder
public class CommentDto {
    private Integer id;
    private String author;
    private String password;
    private String content;
    // + BASETIME
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // + FK 
    private Integer boardId;
}
