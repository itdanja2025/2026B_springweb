package example.day06;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public class Exam {
    public static void main(String[] args) {
        // [1] 리터럴/상수 = 기본값 
        int a = 3; // a변수가 3 참조한다.
        int b = 3; // b변수가 3 참조한다.
        // 두 변수가 참조하는 값은 총 몇개인가요? 1개
        // [2] 참조란? 어떠한 값의 위치 , 인스턴스(객체) 1개 당 참조값 1개
        String c = new String("유재석");
        String d = new String("강호동");
        // 두 변수가 참조하는 값은 총 몇개인가요? 2개
        Test t = new Test();
        t.name = new String("유재석");
        // t 변수가 참조하는 값은 총 몇개인가요? 1개( 1개 ), t -> Test(101번지) -> name(201번지)

        // 자바 참조
        // 1] '자유' 카테고리 등록
        Category c1 = new Category( 1 , "자유" , new ArrayList<>() );
            // c1 참조 몇개 하고 있나요? 1개 , c1 -> category(101번지)
        // 2] '자유' 카테고리에 게시물1 작성
        Board b1 = new Board( 1 , "제목1", c1 );
            // b1 참조 몇개 하고 있나요? 1개 , b1 -> Board(101번지) -> Category(201번지) , String/기본타입 제외
            // *] b1 통해 c1 (참조)알 수 있나요? 가능 , 왜? board에 category 들어있으니까
            // *] JPA 개발자는 이 구조 --> db 연관관계(join) 사용 가능하겠다. 단방향참조(FK) 
            // *] c1 통해 b1 (참조)알 수 있나요? 아니요 , 왜? category 에 board 가 없으니까
        // 3] category 에 board 넣어주자.
        c1.getList().add( b1 );
            // c1 -> category -> list( board )
            // *] c1 통해 b1 (참조)알 수 있나요? 예 
            // *] JPA 서로 참조 가능한 구조 : 양방향참조
        // System.out.println( b1 );
            // b1 -> c1 -> b1 -> c1 -> b1 ~ 무한참조 , <순환참조>
            // 양방향쪽에 @ToString.Exclude 주입한다. 
            // toString( ) 이란? Object(슈퍼)클래스의 객체 주소값 반환 함수
            // + 오버라이딩: 객체 주소값 대신에 문자열로 반환 함수(주로)
        System.out.println( b1 );
            // 데이터베이스는 단방향: 참조[FK]테이블에 PK테이블의 PK값을 저장한다.
            // 데이터베이스는 양방향: X , 참조/매핑 테이블( 실무적으로 권장X )
            // 결론: jpa에서는 양방향 , db는 양방향 없다.
            // 실무에서는 양방향 비권장. 불필요한 자료들까지 불러오니까.~

    }
} // CLASS END 
@Data @AllArgsConstructor
class Board{
    private int bno;
    private String btitle;
    private Category category; // 참조FK
}

@Data @AllArgsConstructor
class Category{ 
    private int cno;
    private String cname;
    @ToString.Exclude // toString 사용금지
    private List<Board> list = new ArrayList<>();
}


class Test{
    String name;
}
