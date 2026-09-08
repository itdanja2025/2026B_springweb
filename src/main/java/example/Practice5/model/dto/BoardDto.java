package example.Practice5.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import example.Practice5.model.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 즉] DTO 역할은 엔티티 자료들을 프론트엔드로 전달
// + 추가적인 자료들을 무엇 주고 받을지 RESTAPI
@NoArgsConstructor@AllArgsConstructor@Data@Builder
public class BoardDto {
    private Integer id;
    private String author;
    private String password;
    private String content;
    // + BASETIME
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // + 달린 댓글들
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();
    // 
    public BoardEntity toEntity(){
        return BoardEntity.builder()
            .content( this.content )
            .author( this.author )
            .password( this.password )
            .build();
    }
    // 
    public static BoardDto from( BoardEntity entity ){
        return BoardDto.builder()
                .id( entity.getId() )
                .author( entity.getAuthor() )
                .password( entity.getPassword() )
                .content( entity.getContent() )
                .createdAt( entity.getCreatedAt() )
                .updatedAt( entity.getUpdatedAt() )
                .build();
    }
}
