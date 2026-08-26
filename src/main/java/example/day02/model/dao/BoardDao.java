package example.day02.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import example.day02.model.dto.BoardDto;

public class BoardDao extends BaseDao {
    private BoardDao(){};
    private static final BoardDao instance = new BoardDao();
    public static BoardDao getInstance( ){ return instance; }

    // [1] 등록 
    public boolean save( BoardDto boardDto ){
        try{// 1. SQL 작성
            String sql = "insert into board( content, writer ) values( ? , ? )";
            // 2. SQL 기재( SQL은 자바가 아니고 외부(MYSQL서버) 전달 )
            PreparedStatement ps = conn.prepareStatement( sql );
            // 3. 기재된 SQL에 매개변수 대입 , ps.set타입( ?순서번호 , 입력받은값 )
            ps.setString( 1 , boardDto.getContent() );
            ps.setString( 2 , boardDto.getWriter() );
            // 4. 기재된 SQL 실행, ps.execute() 단순실행 , .executeUpdate() 실행후반영된레코드수반환
            int result = ps.executeUpdate();
            // 5. SQL 실행 결과
            if( result == 1 ){ return true; } // 만약에 레코드 등록이 1개 되었다면 성공 true
        }catch(Exception e ){ System.out.println( e ); }
        return false; // 등록 실패시 false 
    }

    // [2] 전체조회
    public ArrayList< BoardDto > findAll( ){
        ArrayList<BoardDto> list = new ArrayList<>(); // 레코드1개당 DTO1개 , DTO여러개 -> 배열/리스트
        try{String sql = "select * from board" ;// 1. SQL 작성
            PreparedStatement ps = conn.prepareStatement( sql ); // 2. SQL 기재
            // 3. 기재된 SQL 매개변수 대입 ( SQL내 ? 없어서 생략 )
            ResultSet rs = ps.executeQuery(); // 4. SQL 실행 , insert/update/delete => executeUpdate() , select => executeQuery()
            // 5. SQL 결과 처리
            while( rs.next() ){ // 다음 레코드 뜻 
                BoardDto boardDto = new BoardDto(); 
                boardDto.setNo( rs.getInt("no") ); // rs.get타입( 가져올속성명 );
                boardDto.setContent( rs.getString("content"));
                boardDto.setWriter( rs.getString( "writer") );
                list.add(boardDto);
            }
        }catch(Exception e ){ System.out.println( e ); }
        return list;
    }

    // [3] 개별수정 DAO
    public boolean update( BoardDto boardDto ){
        try{
            String sql = "update board set content = ? where no = ? ";// 1.1 SQL 작성
            PreparedStatement ps = conn.prepareStatement(sql); // 1.2 SQL 기재 *예외*
            ps.setString( 1 , boardDto.getContent() );// 1.3 SQL내 ? 매개변수대입
            ps.setInt( 2 , boardDto.getNo() );
            int result = ps.executeUpdate(); // 1.4 SQL 실행
            if( result == 1 ) return true; // 1.5 실행 결과 반환
        }catch( SQLException e ){ System.out.println( e ); }
        return false; // 1.5 실행 결과 반환
    }

    // [4] 개별삭제 DAO 
    public boolean delete( int no ){
        try{ String sql = "delete from board where no = ?";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , no ); // SQL 문법내 첫번째 ? 에 매개변수 값 대입 
            int result = ps.executeUpdate();
            if( result == 1 ) return true;
        }catch( SQLException e ){ System.out.println( e ); }
        return false;
    }

} // class end 
