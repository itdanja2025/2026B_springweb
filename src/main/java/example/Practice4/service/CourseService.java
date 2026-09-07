package example.Practice4.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice4.model.dto.CourseDto;
import example.Practice4.model.dto.StudentDto;
import example.Practice4.model.entity.CourseEntity;
import example.Practice4.model.repository.CourseRepository;

@Service
public class CourseService {
    @Autowired private CourseRepository courseRepository;

    // 1. 등록
    public boolean 과정등록( CourseDto courseDto ){
        CourseEntity courseEntity = courseDto.toEntity();  // 1. dto --> entity 변환
        CourseEntity savedEntity = courseRepository.save( courseEntity ); // 2. entity 저장하기
        if( savedEntity.getCourseId() >=1 ) return true;// 3. 등록된 entity에 pk 존재하면
        return false;
    }
    // 2. 전체조회
    public List<CourseDto> 과정전체조회( ){
        // 1. findAll 전체조회
        List<CourseEntity> courseEntities = courseRepository.findAll(); 
        // 2. 전체조회 Entity --> dto 변환
        List<CourseDto> courseDtos = new ArrayList<>();
        courseEntities.forEach( (courseEntity) -> { // 2-1: 하나씩 과정엔티티 꺼내서 
            CourseDto courseDto = CourseDto.from(courseEntity); // 2-2: 과정엔티티 -> 과정dto 변환
            // *** 과정DTO에 학생목록 추가!!! ***
            // * 현재 과정(course) ---> 수강기록(enroll)들을 반복하여 --> 수강기록 --> 학생(student)
            courseEntity.getEnrollEntities().forEach( (enroll) -> {
                StudentDto studentDto = StudentDto.from( enroll.getStudentEntity() );
                courseDto.getStudentDtos().add(studentDto);
            });
            courseDtos.add(courseDto); // 2-3 변환된 dto 리스트에 저장
        });
        // 3.
        return courseDtos;
    }
}

