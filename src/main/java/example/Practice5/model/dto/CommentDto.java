package example.Practice5.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import example.Practice5.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long boardId;
    private String author;
    private String password;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // toEntity (PK 및 연관관계 제외)
    public CommentEntity toEntity() {
        return CommentEntity.builder()
                .author(this.author)
                .password(this.password)
                .content(this.content)
                .build();
    }

    // from entity
    public static CommentDto from(CommentEntity entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .boardId(entity.getBoardEntity() != null ? entity.getBoardEntity().getId() : null)
                .author(entity.getAuthor())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
