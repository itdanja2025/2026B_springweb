package example.day13_;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoardEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String fileName;
}