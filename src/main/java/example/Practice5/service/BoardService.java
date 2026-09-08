package example.Practice5.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice5.model.dto.BoardDto;
import example.Practice5.model.dto.CommentDto;
import example.Practice5.model.entity.BoardEntity;
import example.Practice5.model.repository.BoardRepository;

@Service
public class BoardService {
    @Autowired private BoardRepository boardRepository;
    // 1. 
    public boolean 게시물등록( BoardDto boardDto ){
        BoardEntity boardEntity = boardDto.toEntity(); // dto --> entity 
        BoardEntity savedEntity = boardRepository.save( boardEntity ); // entity save 
        if( savedEntity.getId() >= 1 ) return true; // pk가 존재하면 성공 
        return false;
    }
    // 2. 
    public List<BoardDto> 게시물전체조회( ){
        List<BoardEntity> boardEntities = boardRepository.findAll(); // 모든 entity , findAll
        List<BoardDto> boardDtos = new ArrayList<>();
        boardEntities.forEach( (boardEntity) -> {   // 모든 entity -> dto 변환 , 여러번(반복)
            BoardDto boardDto = BoardDto.from(boardEntity); // entity -> dto
            boardEntity.getCommentEntities().forEach((commentEntity) -> { // ** 달린 댓글 포함 **
                CommentDto commentDto = CommentDto.from( commentEntity );
                boardDto.getComments().add(commentDto);
            });
            boardDtos.add(boardDto);
        });
        return boardDtos; 
    }
    // 3. 게시글 번호와 비밀번호를 전달받아 일치 여부 확인 후 삭제한다.
    public boolean  게시물삭제( Integer id , String password ){ // 1. 게시글 번호와 비밀번호 매개변수로 전달받아.
        // 2. 게시글 번호 이용하여 게시물 정보 조회한다. --> 리포지토리 , findById , 엔티티 찾기 
        BoardEntity boardEntity = boardRepository.findById( id ).orElse( null );
        // 3. 게시글 번호 꺼낸 조회 결과 확인
        if( boardEntity != null ){
            if( boardEntity.getPassword().equals( password ) ){
                boardRepository.deleteById( id );
                return true;
            }
        }
        return false;
    }
}
