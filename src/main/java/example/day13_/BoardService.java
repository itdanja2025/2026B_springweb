package example.day13_;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    // [1] 등록
    public boolean boardWrite(BoardDto dto) {
        String savedFileName = null;

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            savedFileName = fileService.fileUpload(dto.getFile());
            if (savedFileName == null) {
                return false;
            }
        }

        try {
            BoardEntity entity = dto.toEntity(savedFileName);
            boardRepository.save(entity);
            return true;
        } catch (Exception e) {
            if (savedFileName != null) {
                fileService.fileDelete(savedFileName);
            }
            System.out.println(e);
            return false;
        }
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

    // [4] 파일명 조회 (다운로드용)
    public String getSavedFileName(Long id) {
        BoardEntity entity = boardRepository.findById(id).orElse(null);
        return entity != null ? entity.getFileName() : null;
    }
}