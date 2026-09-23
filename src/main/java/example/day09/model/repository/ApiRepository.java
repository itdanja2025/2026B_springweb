package example.day09.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.day09.model.entity.ApiEntity;

@Repository 
public interface ApiRepository extends JpaRepository<ApiEntity,Integer>{

}
