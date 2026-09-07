package example.Practice4.model.dto;
import java.time.LocalDateTime;

import example.Practice4.model.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor@AllArgsConstructor@Builder@Data
public class StudentDto {
    private Integer studentId;
    private String studentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // + toEntity  : 학생등록 용도
    public StudentEntity toEntity( ){
        return StudentEntity.builder()
                .studentName(this.studentName)
                .build();
    }
    // + from : 출력 용도
    public static StudentDto from( StudentEntity entity ){
        return StudentDto.builder()
            .studentId( entity.getStudentId() )
            .studentName( entity.getStudentName() )
            .createdAt( entity.getCreatedAt() )
            .updatedAt( entity.getUpdatedAt() )
            .build();
    }
}
