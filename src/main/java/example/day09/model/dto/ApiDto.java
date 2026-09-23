package example.day09.model.dto;

import java.time.LocalDateTime;

import example.day09.model.entity.ApiEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@Data 
@NoArgsConstructor 
@Builder 
public class ApiDto {
    private Integer idx;
    private String subject;
    private String name;
    private String regdate;
    private String content;
    public static ApiDto from(ApiEntity apiEntity){
        return ApiDto.builder()
            .idx( apiEntity.getIdx() )
            .subject(apiEntity.getSubject())
            .name(apiEntity.getName())
            .regdate(apiEntity.getRegdate())
            .content(apiEntity.getContent())
            .build();
    }
    public ApiEntity toEntity( ){
        return  ApiEntity.builder()
                    .name(name).content(content)
                    .subject(subject).regdate( LocalDateTime.now().toString() )
                    .build();
    }
}
