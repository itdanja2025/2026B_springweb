package example.day01;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 1. 해당 컨트롤러에게 HTTP(웹기술) 적용하기 <- 서블릿필요(톰캣포함(스프링부트내장포함))
// 2. 서블릿에게 상속( 해당 클래스로부터 멤버변수/메소드 물려받기) , extends HttpServlet
// 3. 물려받은 기능( init , service , destory ) 재정의 --> 오버라이딩 
// 4. HTTP doXXX 메소드 오버라이딩 하여 기능 구현 --> 컨트롤러내 비지니스로직 구현 ( 컨트롤러역할 ) 
// 5. 해당 컨트롤러에 HTTP 주소 등록하기 , @WebServlet("/주소정의")
@WebServlet( "/example/day01")
public class BoardController extends HttpServlet {
    // [1] 서블릿이 최초 실행 된 경우 딱 1번 실행되는 메소드
    @Override public void init() throws ServletException {  super.init();  }
    // [2] 서블릿이 생성되고 요청마다 (스레드풀에서 스레드할당받아) 실행되는 메소드 
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
    // [3] 서블릿이 사라질 때(서버 종료될 때) 1번 실행되는 메소드 
    @Override public void destroy() { super.destroy(); }
    // ********************** HTTP METHOD CRUD ******************* //
    // [4-1] doGet : HTTP 요청이 GET 이면 실행
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { }
    // [4-2] doPost : HTTP 요청이 POST 이면 실행
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { }
    // [4-3] doPut : HTTP 요청이 PUT 이면 실행
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { }
    // [4-4] doDelete : HTTP 요청이 DETELE 이면 실행
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { }

} // class end 
