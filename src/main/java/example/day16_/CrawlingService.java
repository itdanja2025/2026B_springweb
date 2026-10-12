package example.day16_; // 클래스가 속한 패키지 선언(디렉터리 구조와 일치)

import com.microsoft.playwright.*; // Playwright 프레임워크 핵심 인터페이스(Playwright, Browser, Page 등) 일괄 임포트
import com.microsoft.playwright.options.WaitUntilState; // 페이지 로드 완료 기준 상태(LOAD, DOMCONTENTLOADED 등)를 정의하는 열거형(Enum)
import org.jsoup.Jsoup; // 정적 HTML 문서 파싱 및 간이 HTTP 클라이언트 기능을 제공하는 Jsoup 엔트리포인트
import org.jsoup.nodes.Document; // HTML 문서 전체 DOM 구조를 메모리에 트리 형태로 보관하는 Jsoup 객체
import org.jsoup.nodes.Element; // 단일 HTML 태그 노드를 다루며 속성(attribute) 및 텍스트를 제어하는 객체
import org.jsoup.select.Elements; // ArrayList<Element>를 상속받아 복수 요소를 다루는 Jsoup 전용 컬렉션
import org.springframework.stereotype.Service; // Spring IoC 컨테이너가 비즈니스 로직 빈(Bean)으로 자동 등록/관리하게 만드는 컴포넌트 애너테이션

import java.util.*; // List, Map, Set, ArrayList, HashMap 등 표준 자바 컬렉션 프레임워크 유틸리티

@Service // 싱글톤 서비스 빈으로 스프링 애플리케이션 컨텍스트에 등록
public class CrawlingService { // 웹 데이터 수집 기능을 담당하는 서비스 클래스

    // [1] Jsoup을 사용한 정적 웹페이지 단일 텍스트 목록 크롤링 메서드
    public List<String> craw1() { // 순서가 보장된 문자열 컬렉션을 반환 타입으로 명시
        List<String> list = new ArrayList<>(); // 동적 배열 기반의 ArrayList 객체 초기화
        String url = "https://www.karnews.or.kr/news/articleList.html?sc_section_code=S1N1&view_type=sm"; // GET 요청 대상 엔드포인트 URL 문자열

        try { // I/O 및 네트워크 통신 시 발생 가능한 IOException 등 예외 감지 블록 시작
            Document document = Jsoup.connect(url).userAgent(USER_AGENT).get(); // HTTP GET 커넥션 생성 후 헤더(User-Agent)를 주입하여 Document(DOM 트리)로 파싱 수신
            Elements elements = document.select(".titles > a"); // CSS 선택자(Selector)를 평가하여 조건에 일치하는 모든 <a> 자식 요소를 Elements 컬렉션으로 추출

            for (Element element : elements) { // 컬렉션 내 Element 노드를 하나씩 꺼내는 향상된 for 루프
                String title = element.text().replace("\"", "").trim(); // text()로 태그 제거 후 순수 텍스트만 추출하고, 큰따옴표 제거 및 양 끝 공백 정리
                if (title.isBlank()) { // Java 11+: 빈 문자열이거나 공백으로만 구성되었는지 검증
                    continue; // 빈 데이터는 리스트에 추가하지 않고 다음 반복 회차로 건너뜀
                } // if 조건문 종료

                list.add(title); // 정제된 문자열을 결과 리스트 끝에 추가
            } // for 루프 종료
        } catch (Exception e) { // 크롤링 중 발생한 모든 런타임/체크드 예외 포괄 처리
            e.printStackTrace(); // 표준 에러 출력 스트림에 메서드 호출 스택 추적 로그 출력
        } // catch 블록 종료
        return list; // 수집 및 정제된 제목 리스트 반환
    } // craw1 메서드 종료

    // [2] Jsoup을 이용한 다중 페이지(페이징) 쿼리스트링 및 속성(attr) 데이터 크롤링 메서드
    public List<Map<String, Object>> craw2() { // 도서별 Key-Value 매핑 데이터를 원소로 갖는 리스트 반환
        List<Map<String, Object>> list = new ArrayList<>(); // 전체 도서 데이터(Map)를 누적 보관할 리스트 생성

        try { // 네트워크 호출 및 파싱 예외 처리 블록 시작
            for (int page = 1; page <= 3; page++) { // 반복 변수(page)를 인덱스로 활용하여 1~3페이지 순차 요청
                String url = "https://www.yes24.com/product/category/daybestseller" // 기본 요청 경로(Path)
                        + "?categoryNumber=001" // 특정 카테고리를 지정하는 첫 번째 쿼리 파라미터
                        + "&pageNumber=" + page // 루프 카운터를 문자열과 결합하여 동적으로 페이지 파라미터 구성
                        + "&pageSize=24"; // 한 번에 수신할 항목 개수를 서버에 요청하는 쿼리 파라미터

                Document document = Jsoup.connect(url).userAgent(USER_AGENT).get(); // 대상 페이지로 연결하여 완성된 HTML DOM 트리를 객체화

                Elements nameList = document.select(".info_name .gd_name"); // 하위 선택자(자손)를 통해 도서 제목 태그 목록을 추출
                Elements priceList = document.select(".info_price .txt_num .yes_b"); // 복합 클래스 선택자로 판매가 태그 목록을 추출
                Elements imageList = document.select(".img_bdr .lazy"); // 지연 로딩 태그 클래스를 타깃팅하여 이미지 요소 목록을 추출

                for (int index = 0; index < nameList.size(); index++) { // 동일 인덱스를 공유하는 리스트들을 병합하기 위한 인덱스 기반 for 루프
                    String name = nameList.get(index).text(); // Elements.get(int)로 지정 인덱스 Element에 접근 후 텍스트 추출
                    String price = priceList.get(index).text(); // 해당 순번의 가격 텍스트 추출
                    String image = imageList.get(index).attr("data-original"); // .attr(key): src 대신 Lazy Loading 커스텀 데이터 속성값 추출

                    Map<String, Object> map = new HashMap<>(); // 해시 테이블 기반의 Key-Value 저장소 생성
                    map.put("name", name); // "name" 키에 추출한 제목 문자열 매핑
                    map.put("price", price); // "price" 키에 추출한 가격 문자열 매핑
                    map.put("image", image); // "image" 키에 추출한 원본 이미지 URL 매핑

                    list.add(map); // 완성된 단일 도서 정보 맵을 결과 리스트에 적재
                } // 인덱스 순회 루프 종료
            } // 페이지 반복 루프 종료
        } catch (Exception e) { // 네트워크 단절, 타임아웃, 셀렉터 불일치 등 예외 캐치
            e.printStackTrace(); // 디버깅용 에러 트레이스 출력
        } // catch 블록 종료
        return list; // 누적 수집된 전체 도서 정보 리스트 반환
    } // craw2 메서드 종료

    // HTTP 요청 시 크롤러 감지를 우회하기 위한 표준 브라우저 식별 헤더 상수 선언(불변 공유 메모리)
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36";

    // [3] Playwright(Chromium 엔진)를 활용한 SPA/동적 JS 렌더링 데이터 수집 메서드
    public Map<String, Object> craw3() { // 단일 대상의 다중 속성을 전달할 맵 반환
        Map<String, Object> map = new HashMap<>(); // 결과 데이터를 적재할 맵 초기화
        String url = "https://weather.daum.net/"; // 자바스크립트 실행 후 렌더링되는 대상 URL

        Playwright playwright = Playwright.create(); // Playwright 내부 Node.js 바이너리/드라이버 프로세스 가동

        Browser browser = playwright.chromium().launch( // 크로미움 브라우저 인스턴스 백그라운드 구동
                new BrowserType.LaunchOptions().setHeadless(true).setArgs(List.of("--user-agent=" + USER_AGENT)) // GUI 창 없이 백그라운드 구동(Headless) 및 UA 커스텀 실행 플래그 전달
        ); // 브라우저 프로세스 옵션 빌드 완료

        Page page = browser.newPage(); // 브라우저 컨텍스트 내에서 독립된 탭(Tab) 인스턴스 할당
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)); // URL로 이동하며 HTML 파싱 완료(DOM 준비 시점)까지 로딩 대기

        // 1. 온도 추출
        Locator tempLocator = page.locator(".info_weather .num_deg").first(); // 동적 DOM 요소를 추적하는 지연 바인딩 로케이터 생성 및 첫 번째 노드 선택
        String temp = tempLocator.innerText().trim(); // 화면에 실제 렌더링된 가시 텍스트(Visible Text)를 읽고 여백 제거
        map.put("온도", temp); // 추출된 온도 정보를 맵에 바인딩

        // 2. 미세먼지 추출
        Locator dustLocator = page.locator(".ico_airstat2").first(); // CSS 클래스 셀렉터로 미세먼지 노드 지정
        String dust = dustLocator.innerText().trim(); // 노드 내부 텍스트 추출 및 공백 정리
        map.put("초미세먼지", dust); // 추출된 미세먼지 상태를 맵에 바인딩

        browser.close(); // 브라우저 창 닫기 및 관련 프로세스 리소스 반환
        playwright.close(); // Playwright 백그라운드 통신 데몬 및 엔진 완전 종료
        return map; // 수집된 날씨 정보 매핑 결과 반환
    } // craw3 메서드 종료

    // [4] 무한 스크롤(동적 트리거) 및 다중 텍스트 일괄 수집(Set을 통한 중복 방지) 메서드
    public Set<String> craw4() { // 중복 없는 고유 텍스트 집합을 반환 타입으로 선언
        Set<String> reviewSet = new LinkedHashSet<>(); // 삽입 순서를 유지하면서 중복을 자동으로 제거하는 Set 구현체
        String url = "https://cgv.co.kr/cnm/cgvChart/movieChart/30000927"; // 스크롤 시 추가 데이터를 불러오는 동적 페이지 URL

        Playwright playwright = Playwright.create(); // 브라우저 구동 엔진 인스턴스 초기화

        Browser browser = playwright.chromium().launch( // 브라우저 런처 구성
                new BrowserType.LaunchOptions() // 실행 옵션 빌더 인스턴스 생성
                        .setHeadless(false) // 동작 과정을 육안으로 확인할 수 있도록 GUI 브라우저 창을 직접 노출
                        .setArgs(List.of("--user-agent=" + USER_AGENT)) // 브라우저 프로세스에 봇 차단 방지 헤더 플래그 주입
                        .setSlowMo(3000)); // 각 액션(클릭, 스크롤 등) 사이에 3000ms(3초) 딜레이를 부여해 동작 시각화 및 속도 조절
        Page page = browser.newPage(); // 신규 브라우저 세션/탭 생성
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)); // 타깃 사이트로 이동 및 DOM 트리 구성 대기

        for (int i = 0; i < 5; i++) { // 무한 스크롤 트리거를 5회 발생시키기 위한 루프
            page.evaluate("window.scrollTo(0, document.body.scrollHeight * 0.90)"); // 브라우저 컨텍스트 내에서 직접 JavaScript 스크립트를 실행하여 페이지 하단 90% 지점으로 스크롤
            page.waitForTimeout(3000); // 비동기 API 요청 및 지연 렌더링을 기다리기 위해 스레드를 3초간 명시적으로 대기
        } // 스크롤 루프 종료

        Locator reviews = page.locator(".reveiwCard_txt__RrTgu"); // 일치하는 모든 리뷰 태그 노드를 바라보는 Locator 정의
        for (String text : reviews.allInnerTexts()) { // allInnerTexts(): 매칭된 모든 요소의 화면 텍스트를 List<String> 형태로 일괄 수집하여 순회
            if (!text.isBlank()) { // 유효한 내용이 있는 경우에만 처리
                reviewSet.add(text); // Set 특성을 활용해 중복 텍스트를 배제하며 고유 리뷰만 저장
            } // if 조건문 종료
        } // 텍스트 수집 루프 종료

        browser.close(); // GUI 브라우저 및 하위 탭 정리
        playwright.close(); // 엔진 프로세스 소멸 및 시스템 자원 해제
        return reviewSet; // 중복이 제거된 최종 리뷰 문자열 Set 반환
    } // craw4 메서드 종료
} // CrawlingService 클래스 종료