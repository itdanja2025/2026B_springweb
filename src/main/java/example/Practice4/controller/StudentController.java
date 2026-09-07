package example.Practice4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.service.StudentService;

@RestController@RequestMapping("/api/student")
public class StudentController {
    @Autowired private StudentService studentService;
}
