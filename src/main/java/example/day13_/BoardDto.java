package example.day13_;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private MultipartFile file;
    private String fileName;
    private LocalDateTime createDate; // 프론트에 전달할 작성일자

    public BoardEntity toEntity(String savedFileName) {
        return BoardEntity.builder()
                .title(title)
                .content(content)
                .fileName(savedFileName)
                .build();
    }

    public static BoardDto fromEntity(BoardEntity entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .fileName(entity.getFileName())
                .createDate(entity.getCreateDate())
                .build();
    }
}