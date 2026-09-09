package example.day07;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

interface  Calculator{ int plus( int x , int y ); } // 추상메소드
public class Exam2 {
    public static void main(String[] args) {
        // 1.구현체: 추상메소드(인터페이스) 구현한 객체
        // 2.익명구현체: 추상메소드 구현한 클래스 없이 직접(1회성) 구현
        // 인터페이스 변수명 = new 인터페이스() { 추상메소드구현 };
        Calculator calc = new Calculator() {  @Override public int plus(int x, int y) { return x+y; } };
        int result1 = calc.plus( 5 , 3);
        // 3. 람다표현식
        Calculator calc2 = ( x , y ) -> { return x + y; }; // vs Override public int plus(int x, int y) { return x+y; }
        int result2 = calc2.plus( 5 ,3);
        // 3. 람다표현식 사용하는 인터페이스들
        // ** 제네릭이란? 클래스/인터페이스 안에서 사용할 타입을 정한다.
        // List< MemberDto > , 즉] List에 저장할 자료들의 타입을 MemberDto로 정한다.

        // Function< 인수타입 ,반환타입 > , apply( 인수 )  , 매개변수o / 반환o
        Function< Integer, Integer> function = (x) -> {return x * 2; };
        System.out.println( function.apply( 3 ) ); //6 
        // Supplier< 반환타입 > , .get( ) , 매개변수x / 반환o
        Supplier< Integer>  supplier = ( ) -> { return 2 ; };
        System.out.println( supplier.get() );
        // Consumer< 인수타입 > , accept( 인수 ) , 매개변수o / 반환x
        Consumer< String > consumer = (str) -> { System.out.println( str ); };
        consumer.accept( "유재석" );
        // Predicate< 인수타입 > , test( 인수 ) , 매개변수o / 반환(true/false)
        Predicate< Integer > predicate = ( x ) -> { return x % 2 == 0; };
        boolean result4 = predicate.test( 3 );

        // 활용처: 위 4가지 직접적인 사용이 아닌 API(남이만든 클래스/메소드) 에서 활용가능
        // forEach -> Consumer , map -> Function , filter -> Predicate 
        // 즉] 스트림API 주로 사용된다.
    }   
}
