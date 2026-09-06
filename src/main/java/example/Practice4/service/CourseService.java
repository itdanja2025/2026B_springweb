package example.Practice4.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice4.model.dto.CourseDto;
import example.Practice4.model.dto.StudentDto;
import example.Practice4.model.entity.CourseEntity;
import example.Practice4.model.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // 등록기능1: 과정(Course) 정보 등록
    public boolean courseAdd(CourseDto courseDto) {
        CourseEntity entity = courseDto.toEntity();
        CourseEntity savedEntity = courseRepository.save(entity);
        return savedEntity.getCourseId() != null;
    }

    // 과정 전체 조회 (편의 기능)
    public List<CourseDto> courseFindAll() {
        List<CourseEntity> list = courseRepository.findAll();
        List<CourseDto> dtoList = new ArrayList<>();
        list.forEach( entity -> {
            CourseDto courseDto = CourseDto.from(entity);
            List<StudentDto> studentDtos = new ArrayList<>();
            entity.getEnrollList().forEach( enroll -> {
                
                StudentDto dto = StudentDto.from(enroll.getStudentEntity() );
                studentDtos.add( dto );

            });
            courseDto.setStudentDtos(studentDtos);
            dtoList.add( courseDto );
        });
        return dtoList;
    }
}
