package example.day08.공공데이터;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiService {
    // * 공공데이터 기준 : 로그인 -> 마이페이지 ( 개인 API인증키 )
    String serviceKey = "7540ef37d4d62e8aa1ccaf9c61bc9095638232be0930450a4a0d128c78e9e7d6";
    // * web 요청 객체 만들기 , WebClient 이용한 외부 HTTP 요청/응답
    private final WebClient webClient = WebClient.builder().build();

    // [1] 인천광역시 부평구_맛있는 집(맛집) 현황 JSON ,  ** 서비스키를 주소상에 쿼리스트링으로 포함 **
    // https://api.odcloud.kr/api/15103411/v1/uddi:b7c1c017-1d8d-4b19-8bec-c91a13927ea2?page=1&perPage=10&serviceKey=7540ef37d4d62e8aa1ccaf9c61bc9095638232be0930450a4a0d128c78e9e7d6
    public Map<String, Object > test1(){
        String uri = "https://api.odcloud.kr/api/15103411/v1/uddi:b7c1c017-1d8d-4b19-8bec-c91a13927ea2";
        uri += "?serviceKey="+serviceKey; // 함수 밖에 있는 서비스키 대입
        uri += "&pageNo=1"; // 요청 매개변수1 , 페이지번호
        uri += "&perPage=67"; // 요청 매개변수2 , 페이지당 보여줄 자료 개수 ,
        return webClient.get()
                .uri( uri ) // 요청할 api 주소 넣어준다. url vs uri(매개변수포함)
                .retrieve() // 반환/통신/응답 결과 수신
                .bodyToMono( Map.class ) // 반환 값을 자바 타입으로 변환 , 즉] 반환타입이 JSON 이면 MAP 받는다.
                .block(); // 동기( 처리가 끝날때 까지 대기상태) 방식으로 결과 반환
     }

    // [2] 국립중앙의료원_전국 약국 정보 조회 서비스 XML , ** 서비스키를 주소상에 포함하지 않고 HTTP header 포함 **
    // https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=7540ef37d4d62e8aa1ccaf9c61bc9095638232be0930450a4a0d128c78e9e7d6&pageNo=1&numOfRows=10
    public Map<String,Object> test2(){
        String uri = "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown";
        uri += "?pageNo=1"; // 페이지 번호
        uri += "&numOfRows=10"; // 페이지당 개수
        uri += "&serviceKey="+serviceKey;
        String response = webClient.get()
                .uri( uri ) // 통신할 주소
                //.header( "Authorization" , "Infuser " + serviceKey)
                // HTTP 헤더란? HTTP 통신할때 부가정보 포함하는 정보
                    // 주로 인증 관련된 정보들을 포함하는 경우가 있다. API키, 로그인상태 등
                .retrieve() // 통신 결과/응답 수신/받기
                .bodyToMono( String.class ) // 반환 타입 , XML --> String 타입으로 받아오기
                .block(); // 동기통신
        System.out.println("response = " + response);
        // ************* String( XML ) ---- 변환 ---> MAP/DTO 변환 , JACKSON(XmlMapper) ******************
        try {
            XmlMapper xmlMapper = new XmlMapper(); // xml 매퍼 객체 생성 한다.
                // xmlMapper.readValue( 변환할값 , 변환할타입.class  ); , 예외처리 필수
            Map<String,Object> map = xmlMapper.readValue( response , Map.class); // String(XML) --> MAP 타입변환
            return map; // 반환
        } catch (Exception e) {System.out.println( e ); }
            return null; // 반환
    }
}
/*
    API : 데이터 주고받고 기능을 공유할 수 있는 규칙/프로토콜(HTTP)
    REST API : HTTP 기반의 API
    종류
        1. 개발 : SPRING CONTROLLER
        2. 활용 : 무료/유료 판단
            1) 공공데이터포털 API
            2) LLM(AI모델) API
            3) 사기업: 카카오(지도,로그인 등)  :네이버(로그인,데이터랩 등) :구글(로그인,자동입력방지/캡차),
                번역( DeepL, 파파고 등 ) : 결제( 테스트:아임포트 / 카카오페이 ),:등등
    반환타입 : JSON / XML
    스프링에서 외부HTTP 요청 , 프로젝트/서비스1 <--통신--> 프로젝트/서비스2
        1) https://start.spring.io/
        2) Spring Reactive Web Web
        3) implementation 'org.springframework.boot:spring-boot-starter-webflux'
            - controller : 서버입장의 통신 받는곳
            - webflux : 서버입장에서 먼저 통신 요청
    XML이란 ? < > 괄호 사용한 마크업언어
        - 스프링(자바)에서 타입변환이 필요하다.
        implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.21.1'
*/













