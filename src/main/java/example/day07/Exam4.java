package example.day07;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Exam4 {
    public static void main(String[] args) {
        // 메소드 레퍼런스: 람다식이 단순히 기존메소드만 호출하는 경우 간결하게 표현하는 방법
        // 1. 
        // Integer.parseInt( "문자" );  // 문자 --> 정수 변환 함수
        Function< String , Integer > function1 = ( x ) -> { return Integer.parseInt(x); };
        Function< String , Integer > function2 = Integer::parseInt;
        System.out.println( function2.apply( "10" ) ); // "10" -> 10
        // 2.
        List< String > names = List.of( "유재석","강호동","신동엽","서장훈");
            // 1)
            for( int index = 0 ; index <= names.size() - 1 ; index++ ){ System.out.println( names.get(index) ); }
            // 2)
            for( String name : names ){ System.out.println( name ); }
            // 3)
            names.stream().forEach( (name) -> { System.out.println(name ); } );
            // 4) 메소드참조는 메소드명 명시하고 ()소괄호 작성하지 않는다. 
            names.stream().forEach( System.out::println );
        // 3. 이름들의 글자수 출력하세요
            // 전통 방식
            for( int index = 0 ; index <= names.size()-1 ; index++ ){
                System.out.println( names.get(index).length() ); // 글자수 
            }
            // 스트림 방식 
            names.stream().map( (name) -> {return name.length(); } ).forEach( ( result )->{ System.out.println( result ); } );
            // 메소드 레퍼런스(참조) 방식 , 주의할점은 메소드뒤에 ()소괄호 사용하지 않는다. 내부적으로 메소드 호출 하는 구조 
            names.stream().map( String::length ).forEach( System.out::println );
        // 4. names 리스트내 문자열/이름들을 각각 대입하여 Student 객체 만드세요.
            // 전통방식
            List<Student> list1 = new ArrayList<>();
            for( int index = 0 ; index <= names.size() - 1 ; index++ ){
                Student student = new Student( names.get(index) );
                list1.add( student );
            }
            // 스트림API
            List<Student> list2 = names.stream().map( (name) -> { return new Student(name); } ).toList();
            // 메소드참조(레퍼런스)
            List<Student> list3 = names.stream().map( Student::new ).toList();
            /*
                유형 
                1. 클래스명::static메소드명
                2. 인스턴스명::메소드명
                3. 클래스명::new
                
                JPA 서비스 구조 : entity --> dto 변환
                // 1.
                List<MemberDto> list = entityList.stream().map( ( entity ) -> { return MemberDto.from( entity ); } ).toList();
                // 2.
                List<MemberDto> list = entityList.stream().map( MemberDto::from ).toList();

                return boardRepository.findAll().stream().map( (entity)->{ return BoardDto.from(entity); }  ).toList();
                return boardRepository.findAll().stream().map( BoardDto::from  ).toList();

            */
    }
}
class Student{
    private String name;
    public Student( String name ){ this.name = name; }
}
