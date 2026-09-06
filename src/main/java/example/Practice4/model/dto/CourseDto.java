package example.Practice4.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import example.Practice4.model.entity.CourseEntity;
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
public class CourseDto {
    private Long courseId;
    private String courseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

     private List<StudentDto> studentDtos;

    // toEntity 메소드에서 Id 제외
    public CourseEntity toEntity() {
        return CourseEntity.builder()
                .courseName(this.courseName)
                .build();
    }

    public static CourseDto from(CourseEntity entity) {
        return CourseDto.builder()
                .courseId(entity.getCourseId())
                .courseName(entity.getCourseName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
