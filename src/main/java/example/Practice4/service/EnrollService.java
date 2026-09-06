package example.Practice4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.Practice4.model.dto.EnrollDto;
import example.Practice4.model.entity.CourseEntity;
import example.Practice4.model.entity.EnrollEntity;
import example.Practice4.model.entity.StudentEntity;
import example.Practice4.model.repository.CourseRepository;
import example.Practice4.model.repository.EnrollRepository;
import example.Practice4.model.repository.StudentRepository;

@Service
public class EnrollService {

    @Autowired
    private EnrollRepository enrollRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 등록기능3: 수강(Enroll) 정보 등록
    @Transactional
    public boolean enrollAdd(EnrollDto enrollDto) {
        CourseEntity courseEntity = courseRepository.findById(enrollDto.getCourseId()).get();
        StudentEntity studentEntity = studentRepository.findById(enrollDto.getStudentId()).get();

        EnrollEntity enrollEntity = enrollDto.toEntity();
        enrollEntity.setCourseEntity(courseEntity);
        enrollEntity.setStudentEntity(studentEntity);
        
        EnrollEntity savedEntity = enrollRepository.save(enrollEntity);
        return savedEntity.getEnrollId() != null;
    }

    // 조회기능: 수강번호(enrollId)를 조회하면 학생명과 과정명을 포함한 수강정보를 조회
    public EnrollDto enrollFind(Long enrollId) {
        EnrollEntity entity = enrollRepository.findById(enrollId).get();
        return EnrollDto.from(entity);
    }
}
