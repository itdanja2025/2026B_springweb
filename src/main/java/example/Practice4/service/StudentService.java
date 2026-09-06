package example.Practice4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice4.model.dto.StudentDto;
import example.Practice4.model.entity.StudentEntity;
import example.Practice4.model.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 등록기능2: 학생(Student) 정보 등록
    public boolean studentAdd(StudentDto studentDto) {
        StudentEntity entity = studentDto.toEntity();
        StudentEntity savedEntity = studentRepository.save(entity);
        return savedEntity.getStudentId() != null;
    }

    // 삭제기능: 학생번호(studentId)로 학생(Student) 정보 삭제
    public boolean studentDelete(Long studentId) {
        studentRepository.deleteById(studentId);
        return true;
    }

}
