package example.day13;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // React 로컬 포트 허용
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 등록
    @PostMapping("/write")
    public boolean write( @ModelAttribute BoardDto dto) {
        return boardService.boardWrite(dto);
    }

    // 전체 조회
    @GetMapping("/list")
    public List<BoardDto> list( 
        @RequestParam (name="currentPage") int currentPage ) {
        return boardService.boardFindAll( currentPage );
    }

    // 개별 조회
    @GetMapping("/view")
    public BoardDto view(@RequestParam ( name = "id") Long id) {
        return boardService.boardFindById(id);
    }
    
    private final FileService fileService;
    // [4] 다운로드 
    @GetMapping("/download/{id}")
    public void download( 
        @PathVariable( name="id") Long id , 
        HttpServletResponse response){
        // 1. 다운로드 받을 게시물번호 와 HTTP응답객체 가져온다.
        // 2. 다운로드 받을 게시물번호의 업로드파일명 조회 
        String fileName = boardService.getFileName(id);
        // 3. 만약에 파일명이 존재하면 다운로드 진행 
        if( fileName != null ){
            fileService.fileDownload(fileName, response);
        }
    }

}