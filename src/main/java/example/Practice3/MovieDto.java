package example.Practice3;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@Builder@NoArgsConstructor@AllArgsConstructor // 롬복
public class MovieDto {
    private Integer movieid;
    private String title;
    private String director;
    private LocalDate releasedate;
    private Double rating;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // 1. DTO -> ENTITY : 주로 등록시
    public MovieEntity toEntity( ){ // static 제외한 이유? 해당 메소드 호출하는대상이 dto인스턴스라서
        // return new MovieEntity( null , this.title , this.director, this.releasedate, this.rating);
        // vs
        return MovieEntity.builder().director( this.director ) .title( this.title )
            .rating( this.rating ) .releasedate( this.releasedate )
            .build();
    }
    // 2. ENTITY -> DTO  : 주로 조회시
    public static MovieDto from( MovieEntity entity ){ // static 포함한 이유? 해당 메소드 호출하는대상의 dto 인스턴스가 없는 경우
        return MovieDto.builder()
            .movieid( entity.getMovieid() ).director( entity.getDirector() ).rating( entity.getRating() )
            .title( entity.getTitle() ).releasedate( entity.getReleasedate() )
            .createDate( entity.getCreateDate() ).updateDate( entity.getUpdateDate() )
            .build();
    }
}



















