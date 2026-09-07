package example.day07_0908;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Exam3 {
    public static void main(String[] args) {

        // 메소드 레퍼런스 API : 이미 정의된 메소드를 참조하여 사용하는 표현식
            // 1] 일반메소드 , member.add( 3 , 4 )
            // 2] 람다메소드 , ( x , y ) -> { return x+y; }
            // 3] 레퍼런스API , member :: add( 3 , 4 )

        // [1] static 정적메소드 예시
        int parsedNumber1 = Integer.parseInt( "10" ); // Integer.parseInt( 문자 ); 문자->정수 타입 반환 함수

        Function< String, Integer > function = Integer :: parseInt;   // :콜론 ;세미콜론
        int parsedNumber2 = function.apply( "10" );

        // [2] 일반 메소드 예시
        List< String > names = List.of( "유재석" , "강호동" , "신동엽" , "서장훈" ); // 샘플
            // 2-1 for문
        for( int i = 0 ; i<names.size() ; i++ ){ System.out.println("[2-1] : " + names.get(i)); }
            // 2-2 forEach문
        names.stream().forEach( (x) -> {  System.out.println("[2-2] : " + x); });
            // 2-3 forEach문 + 레퍼런스API
        names.stream().forEach( System.out :: println  );

        // [3] 임의 객체의 인스턴스 메소드 참조 (클래스명::인스턴스메소드)
        // 람다: (str) -> str.length()
        names.stream()
                .map(String::length)
                .forEach(len -> System.out.print(len + " "));
        System.out.println();

        // [3] 생성자 예시
            // 3-1 for 문 , 리스트내 항목(이름) 하나씩 꺼내서 객체 생성
        for( int i = 0 ; i<names.size() ; i++ ){ new Post( names.get(i) ) ; }
            // 3-2 forEach문
        names.stream().forEach( ( x ) -> { new Post( x ); });
            // ** 활용
        // 스트림 + 메소드 참조 방식 (map에서 객체 생성 후 수집)
        // 람다: .map(name -> new Post(name))
        List<Post> postList = names.stream()
                .map(Post::new) // 생성자 참조
                .toList();      // Java 16+ 불변 리스트 변환

        System.out.println("[4] postList = " + postList);


    } // main end
} // class end

class Post {
    private String name;

    public Post(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Post{name='" + name + "'}";
    }
}









