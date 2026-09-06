package example.Practice4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.Practice4.model.dto.CourseDto;
import example.Practice4.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 등록기능1: 과정(Course) 정보 등록
    @PostMapping
    public boolean courseAdd(@RequestBody CourseDto courseDto) {
        return courseService.courseAdd(courseDto);
    }

    // 과정 전체 조회 (편의 REST API)
    @GetMapping
    public List<CourseDto> courseFindAll() {
        return courseService.courseFindAll();
    }
}
