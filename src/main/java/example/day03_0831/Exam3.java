package example.day03_0831;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// [방법 1] 강한 결합 (Tight Coupling) - 직접 new 생성
// 문제점: Dao 구현체가 변경되면 Controller 코드도 수정해야 함, 단위 테스트 어려움
class SampleDao1 {
    void method1() { System.out.println("Dao1 로직"); }
}
class SampleController1 {
    // Controller가 직접 객체 생성 및 생명주기를 관리함
    private SampleDao1 sampleDao1 = new SampleDao1();

    public void method2() {
        sampleDao1.method1();
    }
}
// [방법 2] 고전적 싱글톤 패턴 (Singleton Pattern)
// 장점: 메모리 낭비 방지 (단 하나의 인스턴스 공유)
// 문제점: 여전히 Controller가 Dao의 구체 클래스(getInstance)에 직접 의존하여 결합도가 높음
class SampleDao2 {
    private static final SampleDao2 instance = new SampleDao2();
    private SampleDao2() {} // 외부 new 생성 차단
    public static SampleDao2 getInstance() { return instance; }

    public void method1() { System.out.println("Dao2 로직"); }
}
class SampleController2 {
    private final SampleDao2 sampleDao2 = SampleDao2.getInstance();
    public void method2() {
        sampleDao2.method1();
    }
}
// [방법 3] 스프링 IoC (제어의 역전) & DI (의존성 주입)
// 스프링 컨테이너가 Bean(객체)을 생성 및 관리하고, 필요한 곳에 주입함

// [Bean 등록] 스프링 컨테이너에 싱글톤 스코프로 자동 등록
@Component
class SampleDao3 {
    public void method1() { System.out.println("Dao3 로직 실행"); }
}
// [DI 방식 A] 필드 주입 (Field Injection)
// 특징: 코드가 간결하지만, 외부(순수 자바 테스트 등)에서 주입이 불가능하여 비권장
@Component // Controller 또한 컨테이너가 관리해야 DI가 동작함
class SampleController3 {
    @Autowired
    private SampleDao3 sampleDao3;

    public void method2() {
        sampleDao3.method1();
    }
}
// [DI 방식 B] 수정자 주입 (Setter Injection)
// 특징: 선택적/가변적 의존성에 적합하나, 런타임에 주입 대상이 바뀔 위험이 있음
@Component
class SampleController4 {
    private SampleDao3 sampleDao3;

    @Autowired
    public void setSampleDao3(SampleDao3 sampleDao3) {
        this.sampleDao3 = sampleDao3;
    }

    public void method2() {
        sampleDao3.method1();
    }
}
// [DI 방식 C] 생성자 주입 (Constructor Injection) ★ Spring 공식 권장
// 특징: final 키워드 사용 가능(불변성 보장), 순환 참조 컴파일/기동 시점 발견, 테스트 용이
@Component
class SampleController5 {
    private final SampleDao3 sampleDao3; // final로 불변성 보장

    // Spring 4.3 이후 생성자가 1개만 존재하면 @Autowired 생략 가능
    @Autowired
    public SampleController5(SampleDao3 sampleDao3) {
        this.sampleDao3 = sampleDao3;
    }

    public void method2() {
        sampleDao3.method1();
    }
}
// [DI 방식 D] 롬복(Lombok)을 활용한 실무형 생성자 주입
// 특징: final이 붙은 필드를 모아 생성자를 자동 생성 (@RequiredArgsConstructor)
@Component
@RequiredArgsConstructor
class SampleController6 {
    private final SampleDao3 sampleDao3;

    public void method2() {
        sampleDao3.method1();
    }
}
