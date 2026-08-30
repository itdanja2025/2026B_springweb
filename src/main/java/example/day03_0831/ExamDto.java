package example.day03_0831;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamDto {
    private String name;
    private int age;
}
