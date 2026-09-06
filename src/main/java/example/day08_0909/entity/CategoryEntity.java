package example.day11.todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder // 롬복
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseTime {
    @Id // PK
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // auto_increment
    private Integer cno;
    @Column( length = 100, unique = true, nullable = false  ) // not null
    private String cname;

    @OneToMany( mappedBy = "categoryEntity")
    @Builder.Default
    @ToString.Exclude
    private List<TodoEntity> todoEntityList = new ArrayList<>();
}
