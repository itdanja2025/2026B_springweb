package example.Practice4.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.Practice4.model.entity.CourseEntity;

@Repository
public interface CourseRepository extends JpaRepository< CourseEntity , Integer > {
    
}
