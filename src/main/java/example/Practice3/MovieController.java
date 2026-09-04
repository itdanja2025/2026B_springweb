package example.Practice3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    @Autowired private MovieService movieService;

    @PostMapping("/api/movie")
    public boolean 영화등록( 
            @RequestBody MovieDto movieDto ){
        return movieService.영화등록( movieDto );
    }
    @GetMapping("/api/movie")
    public List<MovieDto> 영화전체조회( ){
        return movieService.영화전체조회( );
    }
    @GetMapping("/api/movie/detail")
    public MovieDto 영화개별조회( 
        @RequestParam( name="movieid") int movieid ){
        return movieService.영화개별조회( movieid );
    }
    @PutMapping("/api/movie")
    public boolean 영화수정( @RequestBody MovieDto movieDto){
        return movieService.영화수정( movieDto );
    }
    @DeleteMapping("/api/movie/{movieid}")
    public boolean 영화삭제( 
        @PathVariable( name="movieid") int movieid ){
        return movieService.영화삭제( movieid );
    }

} // class end 
