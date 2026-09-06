package example.Practice4.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "enroll")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollId; // 수강번호(PK)

    @Column(nullable = false, length = 50)
    private String status; // 수강상태

    // 연관관계 : Enroll(N) ↔ Course(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id") // 과정번호(FK)
    private CourseEntity courseEntity;

    // 연관관계 : Enroll(N) ↔ Student(1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id") // 학생번호(FK)
    private StudentEntity studentEntity;
}
