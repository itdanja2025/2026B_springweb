package example.Practice6.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long bno;
    private String name;
    private Integer price;
    private Long cno;
    private String categoryname;
}