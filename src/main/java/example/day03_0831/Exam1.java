package example.day03_0831;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

// [1] 상위 / 하위 클래스 및 표준 어노테이션
class SuperClass {
    public void method1() {
        System.out.println("SuperClass의 method1 실행");
    }
}
class SubClass extends SuperClass {
    // @Override: 컴파일러에게 부모 메서드를 정확히 재정의했는지 검증하도록 요청 (오타 방지)
    @Override
    public void method1() {
        System.out.println("SubClass에서 재정의된 method1 실행");
    }
    // @Deprecated: 더 이상 사용을 권장하지 않음을 컴파일러 및 IDE에 알림 (호출 시 취소선 표시)
    @Deprecated
    public void method2() {
        System.out.println("더 이상 사용되지 않는 method2 실행");
    }
}
// [2] 커스텀 어노테이션 정의
@Retention(RetentionPolicy.RUNTIME) // RetentionPolicy.RUNTIME: 실행 중(런타임)에도 바이트코드와 메모리에 남아 리플렉션으로 조회 가능
@Target(ElementType.METHOD) // ElementType.METHOD: 메서드 선언부에만 부착 가능
@interface Annotation1 {
    // 어노테이션 속성 (default를 주면 생략 가능)
    String value1() default "기본값";
}
// [3] 커스텀 어노테이션 적용 클래스
class TestClass1 {
    // 어노테이션은 자체로 로직을 실행하지 않고, 메타데이터(부가 정보)를 메서드에 라벨처럼 부착합니다.
    @Annotation1(value1 = "어노테이션 메타데이터 주입 성공!")
    public void method3() {
        System.out.println("TestClass1의 method3() 비즈니스 로직 실행");
    }
}

// [4] 실행 및 리플렉션을 통한 어노테이션 구동
public class Exam1 {
    public static void main(String[] args) throws Exception {
        // --- 1. 표준 어노테이션 확인 ---
        SubClass subClass = new SubClass();
        subClass.method1(); // 오버라이딩된 메서드 호출
        subClass.method2(); // @Deprecated로 인해 IDE에서 취소선(strikethrough) 표시
        // --- 2. 리플렉션(Reflection)을 통한 어노테이션 메타데이터 파싱 ---
        // 2-1. TestClass1의 클래스 정보(메타데이터) 획득
        Class<TestClass1> clazz = TestClass1.class;
        // 2-2. method3 메서드 객체 가져오기
        Method method = clazz.getMethod("method3");
        // 2-4. 붙어있는 어노테이션 객체 추출
        Annotation1 annotation = method.getAnnotation(Annotation1.class);
        // 2-5. 어노테이션에 주입된 값 읽기
        System.out.println("[메타데이터 확인] value1: " + annotation.value1());
        // 2-6. 대상 객체 생성 후 동적 호출 (Spring Framework의 내부 구동 방식과 유사)
        TestClass1 targetObj = clazz.getDeclaredConstructor().newInstance();
        method.invoke(targetObj);
    }
}