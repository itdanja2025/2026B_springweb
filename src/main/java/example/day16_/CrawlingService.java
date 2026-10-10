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
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    // [1] Jsoup 이용한 특정 url 뉴스 타이틀 수집 메서드
    public List<Map<String, Object>> craw1() { // 뉴스 제목 맵들을 담은 리스트를 반환하는 메서드 선언
        List<Map<String, Object>> list = new ArrayList<>(); // 최종 수집된 기사 정보를 담을 빈 결과 리스트 생성
        String url = "https://www.karnews.or.kr/news/articleList.html?sc_section_code=S1N1&view_type=sm"; // 크롤링 대상 뉴스 목록 웹페이지 주소 설정

        try { // 네트워크 통신 및 HTML 파싱 중 발생할 수 있는 예외 처리 블록 시작
            Document document = Jsoup.connect(url).userAgent(USER_AGENT).get(); // 해당 URL로 HTTP GET 요청을 보내고 HTML 문서를 DOM 객체로 파싱
            Elements elements = document.select(".titles > a"); // CSS 선택자를 사용하여 class가 'titles'인 요소 직계 하위의 <a> 태그 목록 추출

            for (Element element : elements) { // 추출된 모든 <a> 링크 태그 요소를 하나씩 순회
                String title = element.text().replace("\"", "").trim(); // 태그 안의 텍스트를 추출하고 큰따옴표 제거 및 앞뒤 공백 정리
                if (title.isBlank()) { // 정제된 기사 제목이 비어있거나 공백만 있는 경우 검사
                    continue; // 유효하지 않은 데이터는 저장하지 않고 다음 태그로 건너뜀
                } // if 조건문 종료

                Map<String, Object> map = new HashMap<>(); // 단일 기사 제목 정보를 키-값 형태로 묶기 위한 Map 객체 생성
                map.put("title", title); // "title" 키에 정제된 기사 제목 텍스트 저장
                list.add(map); // 생성한 기사 맵 객체를 최종 반환 리스트에 추가
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

                Document document = Jsoup.connect(url).userAgent(USER_AGENT).get(); // 파라미터가 포함된 주소로 연결하여 해당 페이지 HTML 가져오기

                Elements nameList = document.select(".info_name .gd_name"); // 도서 제목이 들어있는 HTML 요소들 일괄 추출
                Elements priceList = document.select(".info_price .txt_num .yes_b"); // 판매 가격이 들어있는 HTML 요소들 일괄 추출
                Elements imageList = document.select(".img_bdr .lazy"); // 도서 표지 이미지 태그들 일괄 추출

                for (int index = 0; index < nameList.size(); index++) { // 현재 페이지에서 추출한 도서 개수만큼 순회
                    String name = nameList.get(index).text(); // 해당 순번 요소의 도서 제목 텍스트 추출
                    String price = priceList.get(index).text(); // 해당 순번 요소의 판매 가격 텍스트 추출
                    String image = imageList.get(index).attr("data-original"); // 지연 로딩 속성(data-original)에 저장된 원본 이미지 URL 추출

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

    // [3] Playwright - 다음 날씨 동적 정보 수집 메서드
    public List<Map<String, Object>> craw3() { // 날씨 정보 맵 리스트를 반환하는 메서드 선언
        List<Map<String, Object>> list = new ArrayList<>(); // 일관된 반환 타입을 맞추기 위한 최상위 리스트 생성
        Map<String, Object> map = new HashMap<>(); // 날씨 상세 항목(온도, 미세먼지)을 보관할 Map 객체 생성
        String url = "https://weather.daum.net/"; // 동적 렌더링이 필요한 다음 날씨 페이지 주소

        Playwright playwright = null; // finally 블록에서 자원 해제를 하기 위해 Playwright 참조 변수를 외부에 선언
        Browser browser = null; // 브라우저 프로세스 종료를 위해 Browser 참조 변수를 외부에 선언

        try { // 브라우저 기동 및 크롤링 시 발생 가능한 예외 처리 블록 시작
            playwright = Playwright.create(); // Playwright 드라이버 프로세스 인스턴스 생성
            browser = playwright.chromium().launch( // 크로미움 브라우저 엔진 실행
                    new BrowserType.LaunchOptions().setHeadless(true) // 백그라운드 무헤드(Headless) 모드로 브라우저 실행
            ); // 브라우저 옵션 설정 종료
            Page page = browser.newPage(); // 브라우저 내 새로운 빈 탭(Page) 생성
            page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)); // 페이지 주소로 이동하며 기본 DOM 로드가 끝날 때까지 대기

            // 1. 온도 추출
            Locator tempLocator = page.locator(".info_weather .num_deg, .wrap_weather .num_deg").first(); // 온도 수치 표시 클래스를 첫 번째 요소로 타겟팅
            String temp = tempLocator.innerText().trim(); // 해당 요소의 내부 텍스트를 읽어온 뒤 양 끝 여백 제거
            map.put("온도", temp); // 맵 객체에 "온도" 키로 추출된 값 저장

            // 2. 미세먼지/초미세먼지 추출
            Locator dustLocator = page.locator(".list_air li, .wrap_air .txt_state, [class*='ico_airstat']").first(); // 상태 변화에 영향을 받지 않는 공기질 영역 로케이터 설정
            try { // 공기질 비동기 렌더링 지연에 대비한 국소 예외 처리 블록 시작
                dustLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000)); // 공기질 요소가 DOM에 올라올 때까지 최대 5초간 대기
                String dust = dustLocator.innerText().trim(); // 노출된 공기질 상태 텍스트 추출 및 여백 정리
                map.put("초미세먼지", dust); // 정상 추출 시 "초미세먼지" 키로 맵에 저장
            } catch (Exception ex) { // 5초 초과 또는 요소 부재 시
                map.put("초미세먼지", "정보 없음"); // 프로그램 중단 대신 기본 대체 문자열 저장
            } // 공기질 예외 블록 종료

            list.add(map); // 수집된 날씨 정보 Map을 리스트에 단일 요소로 추가

        } catch (Exception e) { // Playwright 제어 중 발생한 전역 예외 처리
            e.printStackTrace(); // 콘솔에 에러 출력
        } finally { // 성공/실패 여부와 상관없이 무조건 실행되어 백그라운드 좀비 프로세스를 방지하는 자원 정리 블록
            if (browser != null) { // 브라우저 객체가 정상 생성되어 실행 중인 경우
                browser.close(); // 브라우저 창 닫기 및 프로세스 강제 종료
            } // browser null 검사 종료
            if (playwright != null) { // Playwright 드라이버가 살아있는 경우
                playwright.close(); // Playwright 드라이버 세션 연결 종료 및 메모리 해제
            } // playwright null 검사 종료
        } // finally 블록 종료
        return list; // 날씨 정보가 담긴 리스트 반환
    } // craw3 메서드 종료

    // [4] Playwright - CGV 영화 관람평 무한 스크롤 수집 메서드
    public List<Map<String, Object>> craw4() { // 관람평 맵 리스트를 반환하는 메서드 선언
        List<Map<String, Object>> list = new ArrayList<>(); // 최종적으로 컨트롤러에 반환할 통일된 규격의 리스트 생성
        Set<String> reviewSet = new LinkedHashSet<>(); // 스크롤 과정에서 발생하는 중복 리뷰를 제거하고 순서를 유지하기 위한 Set 컬렉션 생성
        String url = "https://cgv.co.kr/cnm/cgvChart/movieChart/30000927"; // 크롤링 대상 영화 상세 페이지 주소

        Playwright playwright = null; // 명시적 자원 정리를 위한 Playwright 객체 선언
        Browser browser = null; // 명시적 자원 정리를 위한 Browser 객체 선언

        try { // 무한 스크롤 및 브라우저 제어 예외 처리 블록 시작
            playwright = Playwright.create(); // Playwright 엔진 초기화
            browser = playwright.chromium().launch( // 크로미움 브라우저 인스턴스 실행
                    new BrowserType.LaunchOptions() // 실행 옵션 빌더 생성
                            .setHeadless(false) // 동작 상태를 직접 눈으로 확인하기 위해 실제 브라우저 창 노출
                            .setSlowMo(300) // 사용자의 눈으로 단계별 흐름을 확인할 수 있도록 각 동작 사이에 0.3초 지연 적용
            ); // launch 메서드 종료
            Page page = browser.newPage(); // 새 브라우저 페이지(탭) 오픈
            page.navigate(url); // 대상 CGV 영화 상세 페이지로 이동

            // 1. 리뷰 섹션으로 이동 (Lazy Loading 유도)
            Locator section = page.locator("#reviewSection"); // HTML 상단 콘텐츠 아래에 위치한 실관람평 컨테이너 ID 선택
            section.waitFor(new Locator.WaitForOptions().setTimeout(10000)); // 관람평 부모 섹션이 로딩될 때까지 최대 10초 대기
            section.scrollIntoViewIfNeeded(); // 해당 섹션 영역이 화면에 나타나도록 뷰포트 스크롤 이동

            page.mouse().wheel(0, 500); // 완전한 뷰포트 진입을 위해 마우스 휠을 아래로 500px 추가 회전
            page.waitForTimeout(2000); // 스크롤 도달에 따른 컴포넌트 마운트 및 API 호출 2초 대기

            // 2. 리뷰 카드 요소 대기
            Locator reviews = page.locator("#reviewSection [class*='Card_txt'], #reviewSection [class*='txt']"); // 리뷰 텍스트가 담긴 클래스 요소 로케이터 지정
            reviews.first().waitFor(new Locator.WaitForOptions().setTimeout(10000)); // 첫 번째 리뷰 텍스트 카드가 DOM에 나타날 때까지 최대 10초 대기

            // 3. 스크롤 누적 수집
            for (int i = 0; i < 5; i++) { // 최대 5회에 걸쳐 아래로 스크롤하며 리뷰 수집 반복
                int prevSize = reviewSet.size(); // 이번 회차 스크롤 전까지 수집된 고유 리뷰 개수 기록

                for (String text : reviews.allInnerTexts()) { // 현재 화면 및 DOM에 마운트되어 있는 모든 리뷰 텍스트 목록 순회
                    String clean = text.trim(); // 텍스트 앞뒤 불필요한 공백 제거
                    if (!clean.isBlank() && !clean.equals("실관람평")) { // 공백 데이터 및 섹션 헤더 타이틀 문자열("실관람평") 제외
                        reviewSet.add(clean); // 유효한 리뷰 본문을 Set 컬렉션에 추가 (중복은 자동 제거됨)
                    } // if 조건문 종료
                } // 텍스트 추출 for 문 종료

                reviews.last().scrollIntoViewIfNeeded(); // 현재 로드된 카드 중 가장 마지막 카드로 뷰포트를 강제 이동시켜 하단 트리거
                page.mouse().wheel(0, 1000); // 추가 데이터 수신을 유도하기 위해 마우스 휠을 아래로 1000px 추가 회전
                page.waitForTimeout(1500); // 비동기 API 요청 및 다음 리뷰 렌더링을 위해 1.5초 대기

                if (reviewSet.size() == prevSize && i > 0) { // 스크롤을 내렸음에도 새로운 리뷰가 추가되지 않았다면
                    break; // 마지막 페이지에 도달한 것으로 판단하고 조기 종료
                } // if 탈출 조건문 종료
            } // 스크롤 for 문 종료

            // Set 문자열 목록을 일관된 List<Map<String, Object>> 구조로 변환
            for (String review : reviewSet) { // 중복 제거된 리뷰 문자열들을 하나씩 순회
                Map<String, Object> map = new HashMap<>(); // 응답 규격용 Map 객체 생성
                map.put("review", review); // "review" 키에 리뷰 텍스트 매핑
                list.add(map); // 결과 리스트에 맵 객체 추가
            } // 변환 for 문 종료

        } catch (Exception e) { // 스크롤 또는 브라우저 조작 중 발생한 예외 처리
            e.printStackTrace(); // 콘솔에 예외 스택 출력
        } finally { // 브라우저 창 닫기 및 리소스 회수를 보장하는 블록
            if (browser != null) { // 브라우저 인스턴스가 존재하는 경우
                browser.close(); // 브라우저 프로세스 종료
            } // browser null 검사 종료
            if (playwright != null) { // Playwright 세션이 존재하는 경우
                playwright.close(); // Playwright 드라이버 종료
            } // playwright null 검사 종료
        } // finally 블록 종료

        return list; // 수집된 모든 리뷰 목록 반환
    } // craw4 메서드 종료
} // CrawlingService 클래스 종료