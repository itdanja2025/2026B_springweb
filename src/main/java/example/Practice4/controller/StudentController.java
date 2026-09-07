package example.Practice4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.model.dto.StudentDto;
import example.Practice4.service.StudentService;

@RestController@RequestMapping("/api/student")
public class StudentController {
    @Autowired private StudentService studentService;

    @PostMapping("")
    public boolean 학생등록( 
        @RequestBody StudentDto studentDto){
        return studentService.학생등록(studentDto);
    }
    // http://localhost:8080/api/student?studentId=1
    @DeleteMapping("")
    public boolean 학생삭제(
        @RequestParam( name = "studentId") Integer studentId ){
        return studentService.학생삭제( studentId );
    }

} // class end 
