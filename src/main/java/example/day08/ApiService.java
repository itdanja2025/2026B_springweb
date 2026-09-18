package example.day08;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service 
public class ApiService {
    // 서비스키 안전하게 application.properties 에서 관리, 즉] 프로젝트간 api키는 github push 하지말자!, notion/excel 에서 공유
    // @Value("${application.propertis속성명}")
    @Value("${api.public-data.service-key}")
    private String serviceKey;
    // 2. WebClient 객체 빌더패턴 생성 
    private WebClient webClient = WebClient.builder().build();
    // [1]. 
    public Map<String,Object> test1(){
        // 1. API 주소( 공공데이터 신청한 api 요청 url )
        String url = "https://api.odcloud.kr/api/15052602/v1/uddi:855807e2-fe8a-4e47-8a5a-ce1894e410d7_201909031553";
        url += "?page="+1;
        url += "&perPage="+10;
        url += "&serviceKey="+serviceKey;
        // 3. WebClient 객체 이용한 api 요청 하고 응답받기 
        Map<String,Object> response =  webClient.get() // .http메소드명    http GET메소드
                .uri( url ) // uri는 http 주소상에 자원(쿼리스트링) 까지 포함
                .retrieve() // 요청 결과 반환 결과 수신
                .bodyToMono(Map.class ) // 응답 결과 content-type 직렬화/변환 , JSON -> Map 
                .block(); // 동기화
        return response; // 요청 결과 반환하기
    }
}

/*
    컬렉션프레임워크: List , Set , Map
        - List: 여러개 자료들을 인덱스로 구분하여 하나의 자료에 저장 
            -> [ 값1, 값2, 값3 ]
        - Set : 여러개 자료들을 인덱스(중복값)없이 하나의 자료에 저장 
            -> ( 값1, 값2, 값3 )
        - Map : key와value 한쌍(entry)으로 여러쌍을 하나의 자료에 저장
            -> { 속성명:값1 , 속성명:값2, 속성명:값3 }
    WebClient 객체 : 스프링에서 외부 API 요청 라이브러리 
        1.설치: implementation 'org.springframework.boot:spring-boot-starter-webflux' 
        2.객체: WebClient webClient = WebClient.builder().build();
    클래스명.class: 리플렉션( 특정/해당 클래스정보 반환 ) 
*/

