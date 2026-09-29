package example.day12_;

import lombok.*;

@Setter @Getter @ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class StudentDto {
    private int sno;
    private String name;
    private int kor;
    private int math;
}
