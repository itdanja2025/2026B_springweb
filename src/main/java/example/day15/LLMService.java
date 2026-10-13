package example.day15;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
/*
    LLM : 대규모 언어 모델 => AI 모델
    LLM API : 각 LLM 회사에서 API 제공 , API(유료/무료 구분)\
    * https://aistudio.google.com/ API 키 발급 -> application.properties

*/
@Service @RequiredArgsConstructor 
public class LLMService {
    @Value("${api.gemini-key}") public String geminiApiKey; // 1. 서비스키 불러오기 
    private final WebClient webClient = WebClient.builder().build(); // 2. api 호출 라이브러리 
    private ObjectMapper objectMapper = new ObjectMapper(); // 3. 직렬화 라이브러리

    // 1. LLM API 호출 메소드
    public  String LLMCall( String prompt ){
        // 1) 프롬프트 구성 
        Map<String, Object> requestBody = 
            Map.of( "contents" , 
                    List.of( Map.of("parts" , 
                        List.of( Map.of( "text" , prompt )))));
        // 2) WebClient 이용하여 LLM API 모델 호출 
        Map<String,Object> response = webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key={key}", geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve().bodyToMono(Map.class)
                .block();
        System.out.println( response );
        // {candidates=[{content={parts=[{text=안녕하세요! 무엇을 도와드릴까요? ?, thoughtSignature=EmAKXgFpFH0TQ01aUdoH+HffHHq1Xgehp5Xpn6DIfm/+FDvttIKFOBkYAJDkSRu5+t5LVb+2Mr4F9U/JaLCBRLWxVW5jb+jRva5JCGESRH9vhpF4wbV2HLTiroKnvcdj9JU=}], role=model}, finishReason=STOP, index=0}], usageMetadata={promptTokenCount=2, candidatesTokenCount=9, totalTokenCount=11, promptTokensDetails=[{modality=TEXT, tokenCount=2}], serviceTier=standard}, modelVersion=gemini-3.5-flash-lite, responseId=FELMaoOtN_aN0-kP3-6joQM}
        // 응답 자료에서 text속성값만 추출 
        JsonNode root = objectMapper.valueToTree(response); // MAP -> JSON 
        JsonNode textNode = root.at("/candidates/0/content/parts/0/text"); // AI응답 본문 추출 
        return textNode.asText(); // 응답자료내 AI응답 본문만 반환 
    }

    // 2. 프롬프트 요청하여 응답받기
    public String chat1( String prompt ){
        return LLMCall(prompt);
    }
    // 3. 데이터베이스내 자료 + 프롬프트 조합 
    private final DeliveryLogRepository deliveryLogRepository;
    public String chat2( ){
        List< DeliveryLog > logList = deliveryLogRepository.findAll(); // 1) 로그 엔티티 모두 꺼내기 
        try{
            String jsonContext = objectMapper.writeValueAsString(logList); // 2) 엔티티 -> JSON 변환 
            // 3. 프롬프트 작성.
            // """ 문자열(프롬프트) """+DB자료
            String prompt = """
                    배송 및 반품 로그 데이터이며 데이터들을 분석하여 아래와 같이 보고서 작성하기
                    1.배송 일수 가 길거나 반품률이 높은 지점 식별
                    2.파손, 배송 지연, 단순 변심 등 반품 패턴 분석
                    3.배송사 별로 배송 통계(개수,평균,반품비율)
                    [데이터]
                    """+jsonContext; // 프롬프트 + db자료 
            return LLMCall(prompt); // 조합된 프롬프트 전달
        }catch(Exception e ){ System.out.println( e );}
        return null;
    }
    
    // 3. 프롬프트 연결( 질문->답->질문->답->질문-답 )
    public String chat3( String topic ){
        // 1. 프롬프트1 작성,  // String.format( "문자열 형식문자1 문자열 " , 값1 );
        String prompt1 = String.format( """
                고객 배송 불만 접수 내용 : %s
                위 내용에서 1.귀책 사유 2.상품 피해 심각도(상/중/하) 판단해 3줄 요약해줘.
                """ , topic);
        String result1 = LLMCall(prompt1);
        // 2. 프롬프트2 작성 
        String prompt2 = String.format( """
                접수 내용 : %s
                1차 분석 결과 : %s
                위 분석을 토대로 배송팀이 구체적인 조치 방안 제안해줘.
                """ , topic , result1 );
        String result2 = LLMCall(prompt2);
        // 3. 프롬프트3 작성 
        String prompt3 = String.format("""
            1차 분석 : %s
            2차 조치 방안: %s
            위 내용으로 고객에게 보낼 친절한 메시지 초안 작성 
            """, result1 , result2 );
        String result3 = LLMCall( prompt3 );
        return result3;
    }

    // 4. 프롬프트 의도 판단 하여 기능 라우팅 + AI에이전트
    // 프롬프트 : 명령어            , LLM : AI모델 , 
    // LLM API : AI모델 REST사용   , AI에이전트: LLM + 외부도구:자율실행
    // 프롬프트 잘 작성하는법? 자료이해도 
    public String chat4( String message ){ 
        // [처리할방법] / [결과 형식] 포함
        String prompt = String.format("""
            배송 CS 로그 서비스 이며, 고객의 요청을 분석하여 
            다음 4가지 action_type 중 하나를 선택한다.
            JSON 형식만으로 반환한다.
            [ 선택 가능한 ACTION_TYPE]
             - TRACK_DELIVERY : 배송 위치 추적 , 도착 예정일 , 어디쯤 왔는지 확인 
             - RETURN_REQUEST : 반품신청 , 단순 변심 회수, 교환 문의
             - CLAIM_COMPENSATION : 배송지연 보상, 상품 파손 보상, 사고 배상 문의 
             - NONE : 위 3가지 해당 하지 않거나 알 수 없는 경우 
            [ JSON 출력 형식 ]
            { "action_type" : "TRACK_DELIVERY" , "order_id" : "추출한 주문번호(예:ORD-202601-001, 없으면 빈문자열)" , "reason":"분류이유"}
            고객 요청 : "%s"
            """, message );
        
        // 2. 프롬프트 전달하여 결과받기
        String response = LLMCall(prompt);
        try{// 3. MAP -> JSON 변환
            JsonNode root = objectMapper.readTree( response );
            // 4. JSON내 "action_type" 추출 , root.path("속성명") : 속성명에 따른 속성값 추출 
            String action_type = root.path("action_type").asText("NONE"); // 없으면 NONE
            // 5. JSON내 "order_id" 추출 
            String orderId = root.path("order_id").asText("");
            // 6. action_type 과 order_id 가지고 각종 서비스 라우팅/연결분기
            if( action_type.equals("TRACK_DELIVERY")){    
                return processOrder( orderId, "고객이 배송 상태 조회를 요청. 위 주문 기록을 확인하고 현재 위치와 배송예정상태를 친절하게 안내해줘.");
            }else if( action_type.equals("RETURN_REQUEST")){
                return processOrder( orderId, "고객이 반품을 요청, 위 주문 기록과 사유를 확인하고 반품 수거 절차 안내해줘." );
            }else if( action_type.equals("CLAIM_COMPENSATION")){
                return processOrder( orderId, "고객이 배송지연 및 파손에 대한 보상 요청, 위 주문 기록의 배송일 수 와 근거로 보상 기준 해당 여부 및 접수 절차 안내해줘.");
            }else{ return "요청을 파악하지 못했습니다. 다시 요청해주세요"; }
        }catch(Exception e ){ System.out.println( e );}
        return "요청 파악하지 못했습니다.";
    } // f end 
    // 각종 서비스 함수
    public String processOrder( String order_id , String prompt ){  
        Optional<DeliveryLog> optional = deliveryLogRepository.findById( order_id ); // 주문번호 찾기 
        if( !optional.isPresent() ){ return "주문번호 없습니다";}
        DeliveryLog deliveryLog = optional.get();
        // 1. 주문정보 엔티티 --> String( JSON ) 변환 
        try{
            String jsonlog = objectMapper.writeValueAsString(deliveryLog);
            // 2. 프롬프트 작성 
            String reqeustPrompt = String.format("""
                [ 주문 정보 ] %s 
                [ 요청 사항 ] %s
                """, jsonlog , prompt ); // DB 정보/엔티티 와 요청사항 들을 프롬프트 작성 
            return LLMCall(reqeustPrompt);
        }catch(Exception e ){ System.out.println( e );} return null;
    }

} //  send 
