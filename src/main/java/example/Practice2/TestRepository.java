package example.Practice2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository< TestEntity , Integer > {
    // extends JpaRepository< 조작할엔티티명 , 조작할엔티티PK타입 >
    // 1. 기본 CRUD 제공 받는다. save( ) , findById( ) , findAll( ) , deleteById( )
    // 2. 페이징/정렬 제공 받는다.
    // 3. 쿼리 커스텀 제공 받는다.
}
