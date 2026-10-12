package example.day16_; // 패키지 경로 선언

import com.microsoft.playwright.*; // Playwright 핵심 클래스(Playwright, Browser, Page, Locator 등) 임포트
import com.microsoft.playwright.options.WaitUntilState; // 페이지 탐색 대기 상태 열거형(DOMCONTENTLOADED 등) 임포트
import org.jsoup.Jsoup; // Jsoup HTML 파서 및 HTTP 통신 라이브러리 임포트
import org.jsoup.nodes.Document; // HTML 문서 전체 트리를 표현하는 클래스 임포트
import org.jsoup.nodes.Element; // 개별 HTML 태그 노드를 다루는 클래스 임포트
import org.jsoup.select.Elements; // 여러 Element 객체들을 담는 컬렉션 리스트 클래스 임포트
import org.springframework.stereotype.Service; // 스프링 비즈니스 로직 빈(Service Component) 등록 어노테이션 임포트

import java.util.*; // List, Map, Set, HashMap, ArrayList 등 자바 컬렉션 유틸리티 임포트

@Service // 스프링 컨테이너에 서비스 계층 빈(Bean)으로 등록
public class CrawlingService { // 웹 크롤링 비즈니스 로직을 수행하는 서비스 클래스 정의

    // 요청 시 웹 서버의 봇 차단을 방지하기 위해 실제 크롬 브라우저 정보를 흉내 내는 HTTP User-Agent 상수 선언

    // [1] Jsoup 이용한 특정 url 뉴스 타이틀 수집 메서드
    public List<String> craw1() { // 뉴스 제목 맵들을 담은 리스트를 반환하는 메서드 선언
        List<String> list = new ArrayList<>(); // 최종 수집된 기사 정보를 담을 빈 결과 리스트 생성
        String url = "https://www.karnews.or.kr/news/articleList.html?sc_section_code=S1N1&view_type=sm"; // 크롤링 대상 뉴스
                                                                                                          // 목록 웹페이지 주소
                                                                                                          // 설정

        try { // 네트워크 통신 및 HTML 파싱 중 발생할 수 있는 예외 처리 블록 시작
            Document document = Jsoup.connect(url).get(); // 해당 URL로 HTTP GET 요청을 보내고 HTML 문서를 DOM 객체로 파싱
            Elements elements = document.select(".titles > a"); // CSS 선택자를 사용하여 class가 'titles'인 요소 직계 하위의 <a> 태그 목록 추출

            for (Element element : elements) { // 추출된 모든 <a> 링크 태그 요소를 하나씩 순회
                String title = element.text().replace("\"", "").trim(); // 태그 안의 텍스트를 추출하고 큰따옴표 제거 및 앞뒤 공백 정리
                if (title.isBlank()) { // 정제된 기사 제목이 비어있거나 공백만 있는 경우 검사
                    continue; // 유효하지 않은 데이터는 저장하지 않고 다음 태그로 건너뜀
                } // if 조건문 종료

                list.add(title); // 생성한 기사 맵 객체를 최종 반환 리스트에 추가
            } // for 반복문 종료
        } catch (Exception e) { // 크롤링 과정에서 발생한 모든 예외 감지
            e.printStackTrace(); // 예외 발생 시 콘솔에 에러 스택 트레이스 출력
        } // catch 예외 처리 블록 종료
        return list; // 수집된 뉴스 기사 목록이 담긴 리스트 반환
    } // craw1 메서드 종료

    // [2] Jsoup 이용한 베스트셀러 도서 목록 수집 (페이징) 메서드
    public List<Map<String, Object>> craw2() { // 베스트셀러 도서 정보 맵 리스트를 반환하는 메서드 선언
        List<Map<String, Object>> list = new ArrayList<>(); // 전체 페이지의 도서 데이터들을 누적 저장할 리스트 생성

        try { // 다중 페이지 HTTP 요청 및 파싱 예외 처리 블록 시작
            for (int page = 1; page <= 3; page++) { // 1페이지부터 3페이지까지 순차적으로 3회 반복
                String url = "https://www.yes24.com/product/category/daybestseller" // 예스24 일별 베스트셀러 기본 주소
                        + "?categoryNumber=001" // 종합 카테고리 식별 쿼리 파라미터 추가
                        + "&pageNumber=" + page // 현재 반복 회차의 페이지 번호 파라미터 결합
                        + "&pageSize=24"; // 한 페이지에 노출할 상품 수(24개) 파라미터 결합

                Document document = Jsoup.connect(url).get(); // 파라미터가 포함된 주소로 연결하여 해당 페이지 HTML 가져오기

                Elements nameList = document.select(".info_name .gd_name"); // 도서 제목이 들어있는 HTML 요소들 일괄 추출
                Elements priceList = document.select(".info_price .txt_num .yes_b"); // 판매 가격이 들어있는 HTML 요소들 일괄 추출
                Elements imageList = document.select(".img_bdr .lazy"); // 도서 표지 이미지 태그들 일괄 추출

                for (int index = 0; index < nameList.size(); index++) { // 현재 페이지에서 추출한 도서 개수만큼 순회
                    String name = nameList.get(index).text(); // 해당 순번 요소의 도서 제목 텍스트 추출
                    String price = priceList.get(index).text(); // 해당 순번 요소의 판매 가격 텍스트 추출
                    String image = imageList.get(index).attr("data-original"); // 지연 로딩 속성(data-original)에 저장된 원본 이미지
                                                                               // URL 추출

                    Map<String, Object> map = new HashMap<>(); // 한 권의 도서 속성들을 매핑할 Map 객체 생성
                    map.put("name", name); // "name" 키에 도서 제목 저장
                    map.put("price", price); // "price" 키에 도서 가격 저장
                    map.put("image", image); // "image" 키에 도서 표지 이미지 링크 저장

                    list.add(map); // 완성된 도서 정보 Map을 결과 리스트에 추가
                } // 도서 목록 순회 for 문 종료
            } // 페이지 반복 for 문 종료
        } catch (Exception e) { // 네트워크 오류 또는 요소 선택자 오류 예외 처리
            e.printStackTrace(); // 콘솔에 상세 에러 로그 출력
        } // catch 예외 처리 블록 종료
        return list; // 누적 수집된 도서 목록 반환
    } // craw2 메서드 종료

    // 요청 시 웹 서버의 봇 차단을 방지하기 위해 실제 크롬 브라우저 정보를 흉내 내는 HTTP User-Agent 상수 선언
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36";


    // [3] Playwright - 다음 날씨 동적 정보 수집 메서드
    public Map<String, Object> craw3() { // 날씨 정보 맵 리스트를 반환하는 메서드 선언-
        Map<String, Object> map = new HashMap<>(); // 날씨 상세 항목(온도, 미세먼지)을 보관할 Map 객체 생성
        String url = "https://weather.daum.net/"; // 동적 렌더링이 필요한 다음 날씨 페이지 주소

        Playwright playwright = Playwright.create(); // Playwright 드라이버 프로세스 인스턴스 생성

        Browser browser = playwright.chromium().launch( // 크로미움 브라우저 엔진 실행
                new BrowserType.LaunchOptions().setHeadless(true).setArgs(List.of("--user-agent=" + USER_AGENT)) 
        ); // 브라우저 옵션 설정 종료
        
        Page page = browser.newPage(); // 브라우저 내 새로운 빈 탭(Page) 생성
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)); // 페이지 주소로 이동하며 기본
                                                                                                      // DOM 로드가 끝날 때까지
                                                                                                      // 대기

        // 1. 온도 추출
        Locator tempLocator = page.locator(".info_weather .num_deg").first(); // 온도 수치 표시 클래스를 첫 번째 요소로 타겟팅
        String temp = tempLocator.innerText().trim(); // 해당 요소의 내부 텍스트를 읽어온 뒤 양 끝 여백 제거
        map.put("온도", temp); // 맵 객체에 "온도" 키로 추출된 값 저장

        // 2. 미세먼지/초미세먼지 추출
        Locator dustLocator = page.locator(".ico_airstat2").first(); // 상태 변화에 영향을 받지 않는 공기질 영역 로케이터 설정
        String dust = dustLocator.innerText().trim(); // 노출된 공기질 상태 텍스트 추출 및 여백 정리
        map.put("초미세먼지", dust); // 정상 추출 시 "초미세먼지" 키로 맵에 저장

        browser.close();
        playwright.close();
        return map; // 날씨 정보가 담긴 리스트 반환
    } // craw3 메서드 종료

    public Set<String> craw4() {
        Set<String> reviewSet = new LinkedHashSet<>();
        String url = "https://cgv.co.kr/cnm/cgvChart/movieChart/30000927"; // 동적 렌더링이 필요한 다음 날씨 페이지 주소

        Playwright playwright = Playwright.create();
        // Browser browser = playwright.chromium().launch(
        // new BrowserType.LaunchOptions()
        // .setHeadless(true)
        // .setArgs(List.of( "--user-agent="+USER_AGENT))
        // );

        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(List.of("--user-agent=" + USER_AGENT))
                        .setSlowMo(150));
        Page page = browser.newPage();
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        for (int i = 0; i < 5; i++) {
            page.evaluate("window.scrollTo(0, document.body.scrollHeight * 0.97)");
            page.waitForTimeout(3000);
        }

        Locator reviews = page.locator(".reveiwCard_txt__RrTgu");
        for (String text : reviews.allInnerTexts()) {
            if (!text.isBlank()) {
                reviewSet.add(text);
            }
        }

        browser.close();
        playwright.close();
        return reviewSet;
    }
} // CrawlingService 클래스 종료