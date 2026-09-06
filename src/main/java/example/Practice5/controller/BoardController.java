package example.Practice5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice5.model.dto.BoardDto;
import example.Practice5.service.BoardService;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 게시글 등록 (POST /api/board)
    @PostMapping
    public boolean boardAdd(@RequestBody BoardDto boardDto) {
        return boardService.boardAdd(boardDto);
    }

    // 게시글 목록 조회 (GET /api/board)
    @GetMapping
    public List<BoardDto> boardFindAll() {
        return boardService.boardFindAll();
    }

    // 게시글 삭제 (DELETE /api/board?id=1&password=1234)
    @DeleteMapping
    public boolean boardDelete(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "password") String password) {
        return boardService.boardDelete(id, password);
    }
}
