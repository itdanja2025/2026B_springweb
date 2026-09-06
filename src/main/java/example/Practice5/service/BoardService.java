package example.Practice5.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.Practice5.model.dto.BoardDto;
import example.Practice5.model.dto.CommentDto;
import example.Practice5.model.entity.BoardEntity;
import example.Practice5.model.repository.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 게시글 등록
    @Transactional
    public boolean boardAdd(BoardDto boardDto) {
        BoardEntity entity = boardDto.toEntity();
        BoardEntity savedEntity = boardRepository.save(entity);
        return savedEntity.getId() != null;
    }

    // 게시글 전체 조회 (댓글 목록 포함, 최신순 정렬)
    public List<BoardDto> boardFindAll() {
        List<BoardEntity> list = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BoardDto> dtoList = new ArrayList<>();

        list.forEach(entity -> {
            BoardDto boardDto = BoardDto.from(entity);
            List<CommentDto> commentDtos = new ArrayList<>();
            entity.getCommentList().forEach(comment -> {
                commentDtos.add(CommentDto.from(comment));
            });
            boardDto.setComments(commentDtos);
            dtoList.add(boardDto);
        });

        return dtoList;
    }

    // 게시글 삭제 (비밀번호 일치 여부 확인)
    public boolean boardDelete(Long id, String password) {
        Optional<BoardEntity> optional = boardRepository.findById(id);
        if (optional.isPresent()) {
            BoardEntity entity = optional.get();
            if (entity.getPassword().equals(password)) {
                boardRepository.delete(entity);
                return true;
            }
        }
        return false;
    }
}
