package example.Practice4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice4.model.repository.StudentRepository;

@Service
public class StudentService {
    @Autowired private StudentRepository studentRepository;

}
