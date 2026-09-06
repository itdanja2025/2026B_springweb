package example.Practice4.service;

import java.util.ArrayList;
import java.util.List;

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

    // 학생 전체 조회 (편의 기능)
    public List<StudentDto> studentFindAll() {
        List<StudentEntity> list = studentRepository.findAll();
        List<StudentDto> dtoList = new ArrayList<>();
        list.forEach(entity -> {
            StudentDto dto = StudentDto.from(entity);
            dtoList.add(dto);

        });
        return dtoList;
    }
}
