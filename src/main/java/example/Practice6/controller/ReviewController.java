package example.Practice6.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice6.dto.ReviewDto;
import example.Practice6.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/reviews")
@CrossOrigin ( value = "http://localhost:5173")
@RequiredArgsConstructor 
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping 
    public List<ReviewDto> getReviews(@RequestParam ("bno") Long bno) {
        return reviewService.findByBno(bno);
    }

    @PostMapping 
    public ReviewDto createReview(@RequestBody ReviewDto dto) {
        return reviewService.save(dto);
    }

    @DeleteMapping 
    public boolean deleteReview(@RequestParam("rno") Long rno) {
        return reviewService.delete(rno);
    }
}