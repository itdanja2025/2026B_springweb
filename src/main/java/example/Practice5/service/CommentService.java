package example.Practice5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice5.model.dto.CommentDto;
import example.Practice5.model.entity.BoardEntity;
import example.Practice5.model.entity.CommentEntity;
import example.Practice5.model.repository.BoardRepository;
import example.Practice5.model.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;
    @Autowired private BoardRepository boardRepository;
    // 1. 댓글 등록 : FK 값 --> FK 엔티티 로 변경
    public boolean 댓글등록( CommentDto commentDto ){
        CommentEntity commentEntity = commentDto.toEntity();
        // ** boardId --> boardEntity 변경
        BoardEntity boardEntity = boardRepository.findById( commentDto.getBoardId() ).orElse(null);
        commentEntity.setBoardEntity( boardEntity ); // ** comment에 FK 엔티티 넣어주기
        CommentEntity savedEntity = commentRepository.save( commentEntity );
        if( savedEntity.getId() >= 1 ) return true;
        return false;
    }
    // 2. 댓글 삭제 :
    public boolean 댓글삭제( Integer commentId , String password ){
        CommentEntity commentEntity = commentRepository.findById(commentId).orElse( null );
        if( commentEntity != null ){
            if( commentEntity.getPassword().equals( password ) ){
                commentRepository.deleteById(commentId);
                return true;
            }
        }
        return false;
    }
}
