package example.Practice5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice5.model.dto.CommentDto;
import example.Practice5.service.CommentService;

@RestController
@RequestMapping("/api/board/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 등록 (POST /api/board/comments)
    @PostMapping
    public boolean commentAdd(@RequestBody CommentDto commentDto) {
        return commentService.commentAdd(commentDto);
    }

    // 댓글 삭제 (DELETE /api/board/comments?commentId=2&password=1234)
    @DeleteMapping
    public boolean commentDelete(@RequestParam(name = "commentId") Long commentId,
                                 @RequestParam(name = "password") String password) {
        return commentService.commentDelete(commentId, password);
    }
}
