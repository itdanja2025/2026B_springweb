package example.Practice4.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import example.Practice4.model.entity.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor@AllArgsConstructor@Builder@Data
public class CourseDto {
    private Integer courseId;
    private String courseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // + 학생목록
    @Builder.Default
    private List<StudentDto> studentDtos = new ArrayList<>();
    // + toEntity : 과정등록 용도
    public CourseEntity toEntity( ){
        return CourseEntity.builder()
                .courseName(this.courseName)
                .build();
    }
    // + from : 출력 용도
    public static CourseDto from( CourseEntity entity ){
        return CourseDto.builder()
                .courseId( entity.getCourseId() )
                .courseName( entity.getCourseName() )
                .createdAt( entity.getCreatedAt() )
                .updatedAt( entity.getUpdatedAt() )
                // 학생목록은 서비스계층 에서~
                .build();
    }
}
