package example.Practice6.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import example.Practice6.dto.ReviewDto;
import example.Practice6.entity.Product;
import example.Practice6.entity.Review;
import example.Practice6.repository.ProductRepository;
import example.Practice6.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional 
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<ReviewDto> findByBno(Long bno) {
        return reviewRepository.findByProduct_Bno(bno).stream()
                .map(r -> ReviewDto.builder()
                        .rno(r.getRno())
                        .bno(r.getProduct().getBno())
                        .reviewer(r.getReviewer())
                        .content(r.getContent())
                        .rating(r.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public ReviewDto save(ReviewDto dto) {
        Product product = productRepository.findById(dto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품 BNO: " + dto.getBno()));

        Review review = Review.builder()
                .product(product)
                .reviewer(dto.getReviewer())
                .content(dto.getContent())
                .rating(dto.getRating())
                .build();

        Review saved = reviewRepository.save(review);
        return ReviewDto.builder()
                .rno(saved.getRno())
                .bno(product.getBno())
                .reviewer(saved.getReviewer())
                .content(saved.getContent())
                .rating(saved.getRating())
                .build();
    }

    public boolean delete(Long rno) {
        if (reviewRepository.existsById(rno)) {
            reviewRepository.deleteById(rno);
            return true;
        }
        return false;
    }
}