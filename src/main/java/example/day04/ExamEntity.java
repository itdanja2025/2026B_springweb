package example.day04;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 엔티티 객체(빈) 등록
@Table( name = "exam" ) // 매핑/연결 할 (DB)테이블의 이름 정의, 생략시 클래스명 자동매핑
@Data@AllArgsConstructor@NoArgsConstructor@Builder // 롬복
public class ExamEntity {
    // 엔티티는 무조건 1개 이상의 PK 갖는다.
    @Id // Primary key 지정
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // auto_increment 지원=mysql
    private Integer eno;
    private String ename;
} // CLASS END 
