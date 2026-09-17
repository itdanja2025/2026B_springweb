package example.Practice6.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long rno;
    private Long bno;
    private String reviewer;
    private String content;
    private Integer rating;
}