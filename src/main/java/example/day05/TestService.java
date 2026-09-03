package example.day05;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired private TestRepository testRepository;
    // 1. 전체조회
    public List<TestDto> 전체조회( ){
        // 1.모든 엔티티 조회한다.    
        List<TestEntity> entities = testRepository.findAll();
        // 2.모든 엔티티 -> DTO 변환하기
        List<TestDto> list = new ArrayList<>(); // 빈 리스트 생성
        // 모든엔티티 반복하여 DTO로 변환하여 새로운 리스트 저장
        entities.forEach( (entity) -> {  // 리스트객체.forEach( (반복변수) -> { } );
            // - 리스트내 하나씩 entity(반복변수)에 대입 반복
            // - TestDto내 entity -> dto 변환함수 : from 함수
            TestDto dto = TestDto.from( entity );
            list.add( dto ); // - 변환 결과 새로운 리스트에 담기
        } );
        return list; // 3. 반환
    }
    // 2. 저장
    public boolean 저장( TestDto testDto ){
        // 1. dto --> entity 변환함수 : toEntity 함수
        TestEntity testEntity = testDto.toEntity();
        // 2. entity save저장
        TestEntity savedEntity = testRepository.save( testEntity );
        // 3. 저장 결과 pk 여부 성공
        if( savedEntity.getNo() >= 1 ){ return true; }
        return false;
    }

}
