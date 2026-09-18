package example.Practice6.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.Practice6.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}