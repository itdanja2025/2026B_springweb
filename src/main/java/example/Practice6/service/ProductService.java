package example.Practice6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.Practice6.dto.ProductDto;
import example.Practice6.entity.Category;
import example.Practice6.entity.Product;
import example.Practice6.repository.CategoryRepository;
import example.Practice6.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAllWithCategory().stream()
                .map(p -> ProductDto.builder()
                        .bno(p.getBno())
                        .name(p.getName())
                        .price(p.getPrice())
                        .cno(p.getCategory().getCno())
                        .categoryname(p.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 CNO: " + dto.getCno()));

        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .category(category)
                .build();

        Product saved = productRepository.save(product);
        return ProductDto.builder()
                .bno(saved.getBno())
                .name(saved.getName())
                .price(saved.getPrice())
                .cno(category.getCno())
                .categoryname(category.getName())
                .build();
    }

    public boolean update(ProductDto dto) {
        Product product = productRepository.findById(dto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품 BNO: " + dto.getBno()));

        Category category = categoryRepository.findById(dto.getCno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 CNO: " + dto.getCno()));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        return true;
    }

    public boolean delete(Long bno) {
        if (productRepository.existsById(bno)) {
            productRepository.deleteById(bno);
            return true;
        }
        return false;
    }
}