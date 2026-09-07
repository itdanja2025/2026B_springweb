package example.Practice4.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity@Table( name="enroll")
@NoArgsConstructor@AllArgsConstructor@Builder@Data
public class EnrollEntity extends BaseTime  {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer enrollId;
    @Column
    private String status;
    // ---------- FK : 과정번호 -------------
    @JoinColumn( name = "course_id") // FK필드명은 주로 연관할 PK필드명과 동일
    @ManyToOne // M:1 , 다:일 , DB에서는 FK표시 되고 JAVA에서는 객체로 표시 
    private CourseEntity courseEntity;
    // ---------- FK : 학생번호 -------------
    @JoinColumn( name = "student_id")
    @ManyToOne
    private StudentEntity studentEntity;
}
