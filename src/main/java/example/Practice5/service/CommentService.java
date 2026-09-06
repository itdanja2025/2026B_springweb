package example.Practice5.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.Practice5.model.dto.CommentDto;
import example.Practice5.model.entity.BoardEntity;
import example.Practice5.model.entity.CommentEntity;
import example.Practice5.model.repository.BoardRepository;
import example.Practice5.model.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    // 댓글 등록
    @Transactional
    public boolean commentAdd(CommentDto commentDto) {
        if (commentDto.getBoardId() == null) {
            return false;
        }

        Optional<BoardEntity> optional = boardRepository.findById(commentDto.getBoardId());
        if (optional.isPresent()) {
            BoardEntity boardEntity = optional.get();
            CommentEntity commentEntity = commentDto.toEntity();
            commentEntity.setBoardEntity(boardEntity);

            CommentEntity savedEntity = commentRepository.save(commentEntity);
            return savedEntity.getId() != null;
        }
        return false;
    }

    // 댓글 삭제 (비밀번호 일치 여부 확인)
    public boolean commentDelete(Long commentId, String password) {
        Optional<CommentEntity> optional = commentRepository.findById(commentId);
        if (optional.isPresent()) {
            CommentEntity commentEntity = optional.get();
            if (commentEntity.getPassword().equals(password)) {
                commentRepository.delete(commentEntity);
                return true;
            }
        }
        return false;
    }
}
