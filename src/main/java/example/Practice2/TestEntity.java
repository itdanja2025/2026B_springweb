package example.Practice2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 데이터베이스 테이블과 자바 클래스와 매핑/연결
@Table( name = "test" ) // 연결할 테이블명 지정,생략시 클래스명
@Data@NoArgsConstructor@AllArgsConstructor@Builder // 롬복
public class TestEntity {
    @Id // PRIMARY KEY
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // AUTO_INCREMENT
    private Integer no;
    private String content;
    private String writer;
}
/*
    JPA 영속성이란? 자바의 데이터가 사라지지 않게 데이터베이스에 저장
        -> 자바 프로그램( 휘발성/영구저장불가능 )
        -> 데이터베이스 프로그램 ( 비휘발성/영구저장가능 )
*/