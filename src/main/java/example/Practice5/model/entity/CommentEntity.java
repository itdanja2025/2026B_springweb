package example.Practice5.model.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name="comment")
@NoArgsConstructor@AllArgsConstructor@Builder@Data
public class CommentEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    private String author;
    private String password;
    private String content;
    // FK란? 다른 테이블(엔티티) 참조
    @ManyToOne // M:1
    @JoinColumn( name = "board_id")
    private BoardEntity boardEntity;
}
