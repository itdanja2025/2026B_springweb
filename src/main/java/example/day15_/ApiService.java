package example.day15_; // 패키지 선언: 현재 클래스가 포함된 패키지 경로를 정의합니다.

import lombok.RequiredArgsConstructor; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service; 
import org.springframework.web.reactive.function.client.WebClient; 
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper; 
import java.util.List;
import java.util.Map; 
import java.util.Optional; 


/*
 ===================================================================================================
 [사전 지식 및 핵심 개념 정리]
 ===================================================================================================
 1. LLM (Large Language Model, 대규모 언어 모델)이란?
    - 방대한 양의 텍스트 데이터를 딥러닝(트랜스포머 아키텍처)으로 사전 학습하여 인간의 언어를 이해, 요약, 번역, 생성할 수 있는 인공지능 모델입니다.
    - 예시: Google Gemini, OpenAI GPT 등.
    - 본 코드에서는 Google Gemini 모델 API(`gemini-3.5-flash-lite`)를 호출하여 배송 분석, 고객 안내문 작성, 의도 분류 등의 추론 엔진으로 활용합니다.

 2. 프롬프트 (Prompt)란?
    - LLM에게 원하는 작업이나 출력을 얻기 위해 입력값으로 전달하는 모든 지시문, 문맥(Context), 질문 텍스트를 뜻합니다.
    - 프롬프트 설계(Prompt Engineering): 질문뿐만 아니라 배경 데이터(DB 레코드 등)와 출력 형식(예: JSON 형식 제약)을 구체적으로 전달하여 AI의 답변 정확도를 제어합니다.

 3. String.format() 이란?
    - 지정한 형식 문자열(Format String) 내의 서식 지정자(%s: 문자열, %d: 정수, %f: 실수 등) 위치에 동적인 변수 값을 대입하여 하나의 완성된 문자열을 만들어내는 Java의 내장 메서드입니다.
    - Java 15 이상에서는 텍스트 블록(""" """)과 결합하여 동적 프롬프트 템플릿을 생성할 때 매우 유용하게 쓰입니다.

 ===================================================================================================
*/

@Service // 스프링이 이 클래스를 비즈니스 로직을 담당하는 Service 빈으로 관리하도록 지정
@RequiredArgsConstructor // final 필드(deliveryLogRepository)를 초기화하는 생성자를 자동으로 생성하여 스프링 의존성 주입 수행
public class ApiService { // 배송 물류 관련 LLM API 비즈니스 로직을 제공하는 서비스 클래스 시작

    @Value("${api.gemini-key}") // application.properties 파일에 설정된 'api.gemini-key' 값을 해당 변수에 주입
    private String geminiApiKey; // 구글 제미나이(Gemini) REST API 호출 시 인증에 사용할 API 키 문자열

    private final WebClient webClient = WebClient.builder().build(); // 외부 HTTP API 호출을 실행할 WebClient 인스턴스를 기본 설정으로 빌드 및 생성
    private final ObjectMapper objectMapper = new ObjectMapper(); // 객체와 JSON 데이터 간의 직렬화/역직렬화를 처리할 ObjectMapper 인스턴스 생성
    private final DeliveryLogRepository deliveryLogRepository; // 데이터베이스의 delivery_log 테이블 접근을 위한 JPA 레포지토리 의존성 주입 필드

    /**
     * [공통 헬퍼] Gemini API 호출 및 순수 텍스트 결과 반환
     */
    private String callGemini(String prompt) { // 작성된 프롬프트를 Gemini API로 전송하고 AI가 생성한 순수 텍스트 응답을 추출해 반환하는 공통 메서드
        Map<String, Object> requestBody = Map.of( // Google Gemini API 요청 규격에 맞는 최상위 JSON 바디 구조(Map) 생성
                "contents", List.of( // 대화 내용 목록을 의미하는 "contents" 키에 리스트 전달
                        Map.of("parts", List.of( // 메시지의 구성 요소를 의미하는 "parts" 키에 리스트 전달
                                Map.of("text", prompt))))); // 실제 텍스트 지시문(프롬프트)을 "text" 키의 값으로 매핑

        Map<String, Object> response = webClient.post() // WebClient를 이용해 HTTP POST 요청 생성
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key={key}", geminiApiKey) // 대상 Gemini 모델의 REST API 엔드포인트 URI 및 쿼리스트링 파라미터로 API 키 바인딩
                .contentType(MediaType.APPLICATION_JSON) // 요청 본문(Payload)의 포맷이 JSON임을 HTTP 헤더에 명시
                .bodyValue(requestBody) // 앞서 구성한 Gemini 규격의 요청 바디 Map 데이터를 HTTP 요청 본문에 적재
                .retrieve() // HTTP 요청을 실행하고 응답을 수신하는 단계로 전환
                .bodyToMono(Map.class) // 응답 본문(JSON)을 Spring의 리액티브 타입인 Mono<Map> 형태로 변환
                .block(); // 비동기 응답이 완료될 때까지 대기(동기식 블로킹)하여 최종 Map 객체 결과 수신

        try { // API 응답 파싱 중 발생할 수 있는 예외 처리를 위한 try 블록 시작
            JsonNode root = objectMapper.valueToTree(response); // Map 형태의 응답 데이터를 Jackson의 JsonNode 트리 구조로 변환
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text"); // JSON 경로 문법을 사용해 AI 생성 텍스트가 위치한 노드로 바로 접근
            if (textNode.isMissingNode()) { // 탐색한 경로에 텍스트 노드가 존재하지 않거나 비어있는지 확인
                return ""; // 노드가 없으면 빈 문자열을 안전하게 반환
            } // 조건문 종료
            return textNode.asText().trim(); // AI가 응답한 텍스트를 문자열로 추출하고 앞뒤 공백을 제거하여 반환
        } catch (Exception e) { System.out.println(e); return "오류발생: "+e; } // JSON 파싱 중 에러 발생 시 콘솔에 출력하고 에러 메시지 반환
    } // callGemini 메서드 종료

    /**
     * [기능 1] 단순 질의 프롬프트: 배송/물류 정책 Q&A
     */
    public String chat1(String prompt) { // 사용자가 입력한 단일 프롬프트를 전달받아 AI의 답변을 받아오는 1번 기능 메서드
        return callGemini(prompt); // 전달받은 프롬프트를 공통 헬퍼 메서드에 전달하여 그 결과를 그대로 반환
    } // test1 메서드 종료

    /**
     * [기능 2] DB 데이터 분석: 배송 지연 및 반품 로그 기반 물류 리포트
     */
    public String chat2() { // DB의 배송/반품 로그 200건 전체를 읽어와 종합 운영 개선 리포트를 작성하는 2번 기능 메서드
        List<DeliveryLog> logList = deliveryLogRepository.findAll(); // 레포지토리를 통해 DB에 저장된 배송 로그(delivery_log) 전체 레코드 조회

        String jsonContext = ""; // DB에서 꺼내온 엔티티 리스트를 JSON 문자열로 저장할 변수 초기화
        try { jsonContext = objectMapper.writeValueAsString(logList); // Java 객체 리스트(logList)를 JSON 문자열 포맷으로 변환(직렬화)
        } catch (Exception e) { System.out.println(e); return "오류발생: "+e; } // 직렬화 실패 시 예외를 콘솔에 출력하고 오류 문자열 반환
        String prompt = """
                다음은 이커머스 배송 및 반품 로그 데이터이다. 데이터를 면밀히 분석하여 운영 개선 보고서를 작성해줘:
                1. [위험 허브 및 택배사]: 배송 소요 일수가 길거나 반품률이 높은 취약 지점 식별
                2. [주요 불만 원인 클러스터링]: 파손, 신선식품 변질, 장기 지연, 단순 변심 등 패턴 분석
                3. [물류 품질 액션 플랜]: 포장 규격 개선, 배송사 계약 관리 등 실무 조치 사항 제시
                
                [데이터]
                """ + jsonContext; // 멀티라인 텍스트 블록으로 분석 지침을 정의하고 하단에 직렬화된 실제 DB 로그 데이터를 결합하여 프롬프트 완성
        return callGemini(prompt); // 조합된 프롬프트를 LLM에 전송하고 종합 리포트 결과 문자열을 반환
    } // test2 메서드 종료

    /**
     * [기능 3] 프롬프트 체인: 배송 클레임 접수 -> 사유 분석 -> 고객 보상 안내문 생성
     */
    public String chat3(String topic) { // 3단계(분석 -> 대책 -> 사과문) 파이프라인으로 순차 처리하는 프롬프트 체이닝 3번 기능 메서드
        // [Step 1] 고객 불만 및 피해 내역 분석
        String step1Prompt = String.format("""
                고객 배송 불만 접수 내용: '%s'
                위 내용에서 (1) 귀책 사유 (택배사 파손/허브 지연/고객 변심), (2) 상품 피해 심각도(상/중/하)를 판단해 3줄로 요약해줘.
                """, topic); // 고객의 클레임 내용(topic)을 주입하여 귀책 사유 및 피해 심각도를 분석하도록 유도하는 1단계 프롬프트 작성
        String step1Result = callGemini(step1Prompt); // 1단계 프롬프트로 LLM을 호출하여 클레임 원인 분석 결과 획득

        // [Step 2] 물류 운영팀 대응 정책 수립
        String step2Prompt = String.format("""
                접수 내용: %s
                1차 분석 결과:
                %s
                
                위 분석을 토대로 물류팀이 취해야 할 구체적인 조치 방안(택배사 보상 청구, 즉시 맞교환, 반품 거부 등) 3가지를 정리해줘.
                """, topic, step1Result); // 원본 접수 내용과 1단계 LLM 분석 결과를 주입하여 실무 조치 방안을 도출하는 2단계 프롬프트 작성
        String step2Result = callGemini(step2Prompt); // 2단계 프롬프트로 LLM을 호출하여 운영팀 실무 대응 방안 결과 획득

        // [Step 3] 고객 발송용 정중한 사과 및 보상 안내문 생성
        String step3Prompt = String.format("""
                1차 분석:
                %s
                2차 조치 방안:
                %s
                
                위 내용을 바탕으로 고객에게 보낼 정중하고 친절한 알림톡/문자 안내 메시지 초안을 작성해줘.
                """, step1Result, step2Result); // 1차 원인 분석과 2차 조치 방안 결과를 주입하여 고객 발송용 최종 안내 메시지를 도출하는 3단계 프롬프트 작성
        String step3Result = callGemini(step3Prompt); // 3단계 프롬프트로 LLM을 호출하여 고객 대상 사과 및 보상 안내문 완성본 획득

        return String.format("### [1단계 클레임 분석]\n%s\n\n### [2단계 운영 대응안]\n%s\n\n### [3단계 고객 발송 안내문]\n%s",
                step1Result, step2Result, step3Result); // 1, 2, 3단계의 모든 추론 결과를 마크다운 헤더로 구조화하여 사용자에게 일괄 반환
    } // executePromptChain 메서드 종료

    /**
     * [기능 4] 3개 기능 라우터: 배송 조회, 반품 접수, 지연/파손 보상 문의 분기 처리
     */
    public String chat4(String userMessage) { // 사용자의 자연어 발화를 분석해 적절한 서비스(조회/반품/보상)로 자동 라우팅하는 4번 기능 메서드
        String routerPrompt = String.format("""
                당신은 이커머스 배송/물류 CS 자동 라우팅 시스템입니다.
                고객의 요청을 분석하여 다음 4가지 action_type 중 하나를 선택하고, JSON 형식만 반환하세요.
                마크다운 코드 블록(```json) 없이 순수 JSON만 출력하세요.
                
                [선택 가능한 action_type]
                - TRACK_DELIVERY: 배송 위치 추적, 도착 예정일 문의, 어디쯤 왔는지 확인
                - RETURN_REQUEST: 반품 신청, 단순 변심 회수, 교환 문의
                - CLAIM_COMPENSATION: 배송 지연 보상금 청구, 상품 파손/변질 피해보상, 택배 사고 배상 문의
                - NONE: 위 3개에 해당하지 않거나 알 수 없는 경우
                
                [JSON 출력 형식]
                {"action_type": "TRACK_DELIVERY", "order_id": "추출한 주문번호(예: ORD-202610-001, 없으면 빈문자열)", "reason": "분류 이유"}
                
                고객 요청: "%s"
                """, userMessage); // 의도 분류 규칙과 엄격한 JSON 출력 포맷을 명시하고 고객 메시지를 주입한 라우터 전용 프롬프트 생성

        String jsonResponse = callGemini(routerPrompt) // LLM 호출 후 모델이 반환한 JSON 문자열 수신
                .replaceAll("```json", "") // 모델이 간혹 추가하는 마크다운 코드 블록 시작 태그(```json) 제거
                .replaceAll("```", "") // 마크다운 코드 블록 종료 태그(```) 제거
                .trim(); // 앞뒤 공백 및 줄바꿈 문자를 완전히 제거하여 순수 JSON 텍스트 확보

        try { // JSON 파싱 및 비즈니스 분기 처리를 위한 try 블록 시작
            JsonNode root = objectMapper.readTree(jsonResponse); // 문자열 형태의 JSON 응답을 읽어 Jackson JsonNode 객체 트리로 파싱
            String actionType = root.path("action_type").asText("NONE"); // JSON에서 "action_type" 값을 추출하며, 없을 경우 기본값 "NONE" 지정
            String orderId = root.path("order_id").asText(""); // JSON에서 추출된 주문번호 "order_id" 값을 가져오며, 없을 경우 빈 문자열 지정

            if ("TRACK_DELIVERY".equals(actionType)) {return trackDeliveryService(orderId); // 의도가 배송 추적일 경우 배송 상태 조회 서비스 호출
            } else if ("RETURN_REQUEST".equals(actionType)) {return requestReturnService(orderId); // 의도가 반품 요청일 경우 반품 회수 접수 서비스 호출
            } else if ("CLAIM_COMPENSATION".equals(actionType)) {return claimCompensationService(orderId); // 의도가 보상 문의일 경우 지연/파손 보상 서비스 호출
            } else {return "요청을 정확히 파악하지 못했습니다. 배송 조회, 반품 접수, 지연/파손 보상 문의 중 원하시는 메뉴를 말씀해 주세요.";} // 유효하지 않은 의도일 경우 폴백 안내 메시지 반환
        } catch (Exception e) { System.out.println(e); return "오류발생: "+e; } // 라우팅 JSON 파싱 실패 또는 예외 발생 시 에러 메시지 반환
    } // routeUserIntent 메서드 종료

    // 물류 비즈니스 연동 (findById 조회 결과 레코드 자체를 프롬프트에 주입)
    private String trackDeliveryService(String orderId) { // [하위 서비스 1] 배송 위치 추적 및 예정일 안내 비즈니스 로직
        return processOrderRequest(orderId, "고객이 배송 상태 조회를 요청했습니다. 위 주문 기록을 확인하고 현재 위치와 배송 예정 상태를 친절하게 안내해줘."); // 공통 조회/프롬프트 처리기에 주문번호와 지시사항 전달
    } // trackDeliveryService 메서드 종료

    private String requestReturnService(String orderId) { // [하위 서비스 2] 반품 신청 및 수거 절차 안내 비즈니스 로직
        return processOrderRequest(orderId, "고객이 반품을 요청했습니다. 위 주문 기록과 사유를 확인하고 반품 수거 절차 및 안내문을 작성해줘."); // 공통 조회/프롬프트 처리기에 주문번호와 반품 접수 지시사항 전달
    } // requestReturnService 메서드 종료

    private String claimCompensationService(String orderId) { // [하위 서비스 3] 배송 지연 및 파손 피해보상 접수 비즈니스 로직
        return processOrderRequest(orderId, "고객이 배송 지연 또는 파손에 대한 보상을 청구했습니다. 위 주문 기록의 배송일수 와 비고 를 근거로 보상 기준 해당 여부 및 접수 절차를 안내해줘."); // 공통 처리기에 주문번호와 보상 심사 지시사항 전달
    } // claimCompensationService 메서드 종료

    /**
     * [단순화 공통 메서드]
     */
    private String processOrderRequest(String orderId, String instruction) { // 주문번호로 DB 레코드를 검증/조회한 뒤 프롬프트에 주입하여 LLM 답변을 생성하는 공통 처리 메서드
        Optional<DeliveryLog> optionalLog = deliveryLogRepository.findById(orderId.trim()); // 공백이 제거된 주문번호(PK)로 DB 테이블에서 단건 조회 수행
        if (!optionalLog.isPresent()) {return "없는 번호 입니다.";} // 데이터베이스에 주문 기록이 존재하지 않으면 즉시 LLM 호출 없이 안내 반환
        DeliveryLog record = optionalLog.get(); // Optional 래퍼에서 실제 DeliveryLog 엔티티 객체 꺼내기
        String jsonRecord = ""; // 엔티티 객체를 직렬화하여 담을 문자열 변수 초기화
        try {jsonRecord = objectMapper.writeValueAsString(record); // 단일 배송 레코드 엔티티를 JSON 문자열로 직렬화
        } catch (Exception e) { System.out.println(e);} // 직렬화 중 에러가 발생하면 콘솔에 출력

        String prompt = String.format("""
                [주문 DB 레코드]
                %s
                
                [요청 사항]
                %s
                """, jsonRecord, instruction); // 실제 조회된 DB 레코드 데이터(JSON)와 각 서비스의 개별 요구사항(instruction)을 결합하여 맞춤형 프롬프트 완성

        return callGemini(prompt); // 데이터가 결합된 프롬프트를 LLM에 전달하여 최종 상황 맞춤형 응답을 얻어 반환
    } // processOrderRequest 메서드 종료
} // ApiService 클래스 종료