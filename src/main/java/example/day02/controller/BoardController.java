package example.day02.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import example.day02.model.dao.BoardDao;
import example.day02.model.dto.BoardDto;

/*
    컨트롤러에 서블릿( HTTP 프로토콜 사용 가능하게 기능/방법( GET/POST/PUT/DELETE ) 제공하는 클래스 ) 기능 달기
        * 레게시(과거) 코드는 상속받아 서블릿 구현
        * 스프링은 @Controller 내 서블릿 포함
    // 1. 웹기술 포함할 컨트롤러 클래스 위에 @Controller 또는 반환타입이 JOSN 이면 @RestController
        * HTTP content type: http 전송 데이터 타입 명시 
        text/html , application/json(@RestController) , form 등등 ( DTO는 없다. )
    // 2. 해당 메소드 마다의 URL 정의
        * URL 정의시 http://127.0.0.1:8080(도메인) 이후 경로(path/url) 정의 , 중복없이 아무거나!
        1. @PostMapping( "/URL") : HTTP 메소드중에 POST 메소드 매핑/연결/대응 어노테이션 
*/
@RestController
public class BoardController {
    private BoardDao bd = BoardDao.getInstance();
    // [1] 등록
    @PostMapping( "/board/save" )
    public boolean save( BoardDto boardDto ){
        boolean result = bd.save(boardDto);
        return result;
    }
    // [2] 전체조회
    @GetMapping( "/board/findall" )
    public ArrayList<BoardDto> findAll( ){
        ArrayList<BoardDto> result = bd.findAll();
        return result;
    }

    // [3] 개별수정 Controller
    @PutMapping( "/board/update" )
    public boolean update( BoardDto boardDto ){
        return bd.update( boardDto );
    }

    // [4] 개별삭제 Controller
    @DeleteMapping( "/board/delete" )
    public boolean delete( int no ){
        return bd.delete( no );
    }
    
} // class end 
