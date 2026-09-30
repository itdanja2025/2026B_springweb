package example.day13_;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // React 로컬 포트 허용
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    // 등록
    @PostMapping("/write")
    public boolean write( BoardDto dto) {
        return boardService.boardWrite(dto);
    }

    // 전체 조회
    @GetMapping("/list")
    public List<BoardDto> list() {
        return boardService.boardFindAll();
    }

    // 개별 조회
    @GetMapping("/view")
    public BoardDto view(@RequestParam ( name = "id") Long id) {
        return boardService.boardFindById(id);
    }

    // 특정 게시물의 첨부파일 다운로드
    @GetMapping("/download")
    public void download(@RequestParam ( name = "id") Long id, HttpServletResponse response) {
        String savedFileName = boardService.getSavedFileName(id);
        if (savedFileName != null) {
            fileService.fileDownload(savedFileName, response);
        }
    }
}