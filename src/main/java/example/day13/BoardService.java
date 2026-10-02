package example.day13;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;
    // [1] 등록
    public boolean boardWrite(BoardDto dto) {
        // 1. 첨부파일 존재하면 업로드 진행
        String savedFileName = null;
        if( dto.getFile() != null && !dto.getFile().isEmpty() ){
            // 2. 파일업로드 후 업로드된 파일명 반환 받기
            savedFileName = fileService.fildUpload( dto.getFile() );
            if( savedFileName == null ) return false; // 다운로드 실패시 null 
        }
        // 3. 엔티티에에 다운로드한 파일명 추가하기 
        BoardEntity entity = dto.toEntity();
        entity.setFileName(savedFileName);
        // 4. 엔티티 저장 
        boardRepository.save(entity);
        return true;
    }
    // [2] 전체 조회
    public List<BoardDto> boardFindAll() {
        return boardRepository.findAll().stream()
                .map(BoardDto::fromEntity)
                .collect(Collectors.toList());
    }
    // [3] 개별 조회
    public BoardDto boardFindById(Long id) {
        BoardEntity entity = boardRepository.findById(id).orElse(null);
        if (entity != null) {
            return BoardDto.fromEntity(entity);
        }
        return null;
    }

    // [4] PK로 첨부파일명 조회
    public String getFileName( Long id ){
        Optional<BoardEntity> optional = 
            boardRepository.findById( id );
        if( optional.isPresent() ){
            return optional.get().getFileName();
        }
        return null;
    }
}