package example.day03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

public class Exam3 {
    public static void main(String[] args) {
    } // main end 
} // class end 

// [1] 전통 방식의 객체 생성 , 계속적인 dao인스턴스 생성가능
class SampleDao{ void method(){ System.out.println("메소드실행"); } }
class SampleController1{ 
    void method(){
        // 1. 다른 클래스내 메소드 호출하는방법
        SampleDao sampleDao = new SampleDao(); // 인스턴스생성(주체)
        sampleDao.method(); // 해당 인스턴스(주체) 가 메소드 호출
    }
}
// [2] 전통 방식의 싱글톤 생성 , dao인스턴스 생성불가능
class SampleDao2{ 
    private SampleDao2(){}
    private static final SampleDao2 instance = new SampleDao2();
    public static SampleDao2 getInstance(){ return instance; }
    public void method(){}
}
class SampleController2{
    SampleDao2 dao2 = SampleDao2.getInstance();
    void method(){
        // 2. 다른 클래스내 메소드 호출하는방법
        dao2.method();
    }
}

// [3] 스프링 IOC/DI
@Component //싱글톤 대신에 @Component 이용하여 IOC(자동객체관리) 규칙으로 스프링 컨테이너에 자동 빈(객체)등록
class SampleDao3{ 
    void method(){}
}
class SampleController3{
    @Autowired // 1.스프링 컨테이너에 등록된 빈(객체) 가져와서 대입
    private SampleDao3 dao3;
    void method(){
        dao3.method();
    }
}

class SampleController4{
    // 2. 스프링에서 권장하는 방식
    private final SampleDao3 dao3;
    @Autowired
    public SampleController4( SampleDao3 dao3 ){
        this.dao3 = dao3;
    }
    void method(){
        dao3.method();
    }
}
@RequiredArgsConstructor // final 멤버변수 생성자 (자동)생성
class SampleController5{
    // 3. 롬복 이용한 방식 , final 초기화 이후 수정불가능
    private final SampleDao3 dao3;
}

/*
    즉] 다른 클래스의 메소드 호출 하기 위해서는 객체필요하다.
    1. [전통] new 클래스명().메소드명();
    2. [미니프로젝트2] 클래스명().getInstance().메소드명();
    3. [스프링] @Component 등록, @Autowired 호출
    ----- 
    @Component 등록되는 시점: @SpringBootApplication( @ComponentScan )
    즉] 스프링이 켜지면서 폴더내 @Componet 다 찾아(같은폴더/하위폴더) 내서 스프링 컨테이너 등록 구조
    AppStart 주의할점: 최상위에 위치한다.
    
*/