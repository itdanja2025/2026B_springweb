package example.Practice4.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.Practice4.model.entity.EnrollEntity;

@Repository
public interface EnrollRepository extends JpaRepository<EnrollEntity, Long> {
}
