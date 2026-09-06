package example.Practice4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.model.dto.StudentDto;
import example.Practice4.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 등록기능2: 학생(Student) 정보 등록
    @PostMapping
    public boolean studentAdd(@RequestBody StudentDto studentDto) {
        return studentService.studentAdd(studentDto);
    }
    // 삭제기능: 학생번호(studentId)로 학생(Student) 정보 삭제 (RequestParam)
    @DeleteMapping
    public boolean studentDeleteParam(@RequestParam(name = "studentId") Long studentId) {
        return studentService.studentDelete(studentId);
    }

    // 학생 전체 조회 (편의 REST API)
    @GetMapping
    public List<StudentDto> studentFindAll() {
        return studentService.studentFindAll();
    }
}
