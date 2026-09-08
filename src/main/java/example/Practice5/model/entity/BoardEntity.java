package example.Practice5.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 즉] 엔티티란? 영속성의 실체 , 데이터베이스 표
// 다르면 연동/매핑 안된다.
@Entity
@Table( name = "board")
@NoArgsConstructor@AllArgsConstructor@Builder@Data
public class BoardEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    private String author;
    private String password;
    private String content;
    // 양방향
    @OneToMany( mappedBy = "boardEntity" , cascade = CascadeType.ALL ) // 1:M , 자바에서 매핑할 FK멤버변수명
    @ToString.Exclude 
    @Builder.Default
    private List<CommentEntity> commentEntities = new ArrayList<>();
}
