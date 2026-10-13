package example.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/*
    크롤링 도구/라이브러리 선택
    [1] 정적페이지(모든자료가 한번에 표시) vs 동적페이지(구분별 자료들이 순차적으로 표시)
        JSOUP                               Playwright, selenium 등등
    [2] 도메인/robots.txt 검색하여 allow가능, Disallow불가능 판단 , 불가능이여도 크롤링 가능 
    [3] *크롤링할 선택자/식별자 찾기* , 크롤링할페이지에서 F12(개발자도구) -> ctrl+shift+c
        크롤링하고 싶은 위치를 선택하여 개발자도구에서 해당 html 찾아서 크롤링 대상 선정
    [4] ip차단 방지 하고자 user-agent 와 적절한대기시간 
*/
@Service 
public class CrawService {
    // * 크롤링 실행 시 클라이언트/브라우저 정보 포함 , 
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36";
    // 1. 정적페이지 크롤링1
    public List<String> craw1( ){
        // 1. 크롤링 페이지 지정
        String url = "https://www.karnews.or.kr/news/articleList.html?sc_section_code=S1N1&view_type=sm";
        // 2. JSOUP 객체 생성 , Jsoup.connect( 크롤링할주소 ).userAgent( 브라우저정보 ).get() , 일반예외
        try{ 
            Document document = Jsoup.connect( url ).userAgent( USER_AGENT ).get();
            // 3. 가져온 html 안에서 식별자( class , id , 마크업 , 자식선택자, 자손선택자 등등 ) 내 값 만 추출 
            // 요소1개추출 = Element , 요소2개이상추출 = Elements
            Elements elements = document.select(".titles > a"); // document.select( 선택자 );
            // 4. 가져온 것들을 반복문 이용하여 리스트에 담기 
            List<String> list = new ArrayList<>();
            for( Element  element : elements  ){
                list.add( element.text() ); // 5. 마크업 사이에 텍스트 추출 : .text()
            }
            return list; 
        }catch(Exception e ){ System.out.println( e );} return null;
    }

    // 2. 정적페이지 크롤링2 
    public List<Map<String,Object>> craw2( ){
        String url = "https://www.yes24.com/product/category/daybestseller";
        try{
            Document document = Jsoup.connect(url).userAgent( USER_AGENT ).get();
            // .gd_name 도서명 , .txt_num > .yes_b 가격 , .img_bdr img 이미지 
            Elements nameList = document.select( ".gd_name");
            Elements priceList = document.select( ".txt_num > .yes_b");
            Elements imgList = document.select( ".img_bdr img");
            List<Map<String,Object>> list = new ArrayList<>();
            for( int index = 0 ; index < nameList.size() ; index++ ){
                Map<String,Object> map = new HashMap<>();
                map.put( "name" , nameList.get(index).text() ); // "name" 키로 index번째 도서명 값 저장 
                map.put( "price" , priceList.get(index).text() ); // "price" 키로 index번째 도서명 값 저장 
                map.put( "image" , imgList.get(index).attr("data-original") ); // "image" 키로 index번째 도서명 값 저장 
                // .text() 마크업사이의텍스트추출 , .attr("특정속성명") 특정속성명의 속성값 추출
                list.add( map );
            }
            return list; 
        }catch(Exception e ){ System.out.println( e );} return null;
    }

} // class end 
