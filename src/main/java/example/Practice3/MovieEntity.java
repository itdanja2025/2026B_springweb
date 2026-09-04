package example.Practice3;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity // 데이터베이스 테이블과 매핑 관계
@Table( name="movie") // 테이블명 지정 
@Getter@Setter@ToString@Builder@NoArgsConstructor@AllArgsConstructor // 롬복
public class MovieEntity extends BaseTime {
    @Id // PRIMARY KEY
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // AUTO_INCREMENT
    private Integer movieid;
    @Column( nullable = false ) // not null 
    private String title;
    @Column( length = 100 ) // varchat(100)
    private String director;
    private LocalDate releasedate;
    private Double rating;
}
