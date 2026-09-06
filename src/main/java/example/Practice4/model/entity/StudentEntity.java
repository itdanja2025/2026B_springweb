package example.Practice4.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId; // 학생번호(PK)

    @Column(nullable = false, length = 50)
    private String studentName; // 학생명

    // 연관관계 : Student(1) ↔ Enroll(N)
    @OneToMany(mappedBy = "studentEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<EnrollEntity> enrollList = new ArrayList<>();
}
