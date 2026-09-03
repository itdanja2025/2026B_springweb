package example.Practice2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service // 해당 클래스가 비지니스로직 객체(빈)등록
public class TestService {
    @Autowired private TestRepository testRepository;

    // [1]
    public boolean testWrite( TestEntity testEntity ){
        // testEntity : 영속전
        // 1. 리포지토리 이용한 insert 처리하기 , .save( 입력받은값이들어있는엔티티 )
        TestEntity savedEntity = testRepository.save( testEntity );
        // 2. save 후 성공시 영속(자바<--연결된상태-->DB) 된 엔티티
        if( savedEntity.getNo() >= 1 ){ return true; }// pk가 존재하면 성공
        return false;
    }
    // [2]
    public List<TestEntity> testPrint( ){
        // 1. 리포지토리 이용한 select 처리하기 , .findAll( )
        List<TestEntity> list = testRepository.findAll();
        return list;
    }
    // [3]
    public TestEntity testDetail( int no ){
        // 1. 리포지토리 이용한 select 처리하기 , .findById( pk번호 )
        // Optional 클래스란? 객체사용시 null 예외 가 발생하는 경우 안전하게 메소드 제공
        Optional<TestEntity> optional = testRepository.findById( no );
        // 2. 포장(Optional)내 엔티티 들어있어? 조회결과 엔티티 확인
        if( optional.isPresent() ){
            // 3. 포장(Optional) 열기, 엔티티 꺼내기
            TestEntity entity = optional.get(); 
            return entity;
        }
        return null; // 객체가 없다는 뜻이 null
    }
}
