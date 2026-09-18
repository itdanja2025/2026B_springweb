package example.Practice6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.Practice6.dto.CategoryDto;
import example.Practice6.entity.Category;
import example.Practice6.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(c -> CategoryDto.builder()
                        .cno(c.getCno())
                        .name(c.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public CategoryDto save(CategoryDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();
        Category saved = categoryRepository.save(category);
        return CategoryDto.builder()
                .cno(saved.getCno())
                .name(saved.getName())
                .build();
    }

    public boolean delete(Long cno) {
        if (categoryRepository.existsById(cno)) {
            categoryRepository.deleteById(cno);
            return true;
        }
        return false;
    }
}