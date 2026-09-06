package example.Practice4.model.dto;

import java.time.LocalDateTime;

import example.Practice4.model.entity.EnrollEntity;
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
public class EnrollDto {
    private Long enrollId;
    private String status;

    private Long courseId;
    private Long studentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String courseName;
    private String studentName;

    // toEntity 메소드에서 Id 제외 (기본)
    public EnrollEntity toEntity() {
        return EnrollEntity.builder()
                .status(this.status)
                .build();
    }

    public static EnrollDto from(EnrollEntity entity) {
        return EnrollDto.builder()
                .enrollId(entity.getEnrollId())
                .status(entity.getStatus())
                .courseId(entity.getCourseEntity().getCourseId())
                .studentId(entity.getStudentEntity().getStudentId())
                .courseName( entity.getCourseEntity().getCourseName() )
                .studentName( entity.getStudentEntity().getStudentName() )
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
