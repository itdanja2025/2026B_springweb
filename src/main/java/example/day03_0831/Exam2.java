package example.day03_0831;

import lombok.*;

public class Exam2 {
    public static void main(String[] args) {
        /*
            롬복 lombok : 반복되는 코드들을 줄여주는 라이브러리
                1. 인텔리제이 설치
                    [ 파일 ] -> [설정] -> [플러그인] -> 'lombok'검색 후 설치
                2. 프로젝트 의존성 추가
                    1. https://start.spring.io
                    2. Dependencies 에서 'lombok' 검색 후 추가
                    3. [ EXPLORE ] ->  롬복 관련 디펜더시 코드만 복사
                        compileOnly 'org.projectlombok:lombok'
                        annotationProcessor 'org.projectlombok:lombok'
                    4. 프로젝트내 build.gradle 파일에 dependencies{ } 붙여넣기
                    5. build.gradle 새로고침
         */
        StudentDto studentDto1 = new StudentDto();
        StudentDto studentDto2 = new StudentDto( 1 , "유재석");
        studentDto2.getSname();
        studentDto2.setSname("강호동");
        studentDto2.toString();

        // * 생성자는 매개변수의 순서대로 인자값 전달해야한다. 유연성 떨어진다.
        // StudentDto studentDto3 = new StudentDto( "유재석" , 1 );
        // 해결책 : 빌더패턴 ( 객체 만드는 패턴 = 유연성 제공 )
        // 클래스명.builder().멤버변수명(값).멤버변수명(값).build();
        StudentDto studentDto4 = StudentDto.builder()
                .sno( 1 ).sname( "유재석" ).build();
        StudentDto studentDto5 = StudentDto.builder()
                .sname( "유재석" ).sno( 2 ).build();
        StudentDto studentDto6 = StudentDto.builder()
                .sname( "유재석" ).build();

    } // main end
} // class end

@NoArgsConstructor // 컴파일(코드번역될때) 기본생성자 코드 자동 생성
@AllArgsConstructor // 컴파일(코드번역될때) 전체매개변수생성자 코드 자동 생성
@Getter // getter 메소드 제공
@Setter // Setter 메소드 제공
@ToString // ToString 메소드 제공
// @RequiredArgsConstructor // final 매개변수 생성자 자동 생성
// @Data = @Getter + @Setter + @ToString + @RequiredArgsConstructor
@Builder // 빌더패턴 사용
class StudentDto{
    // 1. private 멤버변수
    private int sno;
    private String sname;
    // 2. 빈생성자, 풀생성자
    // 3. getter/setter , toString
}
