package example.Practice4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Practice4.model.repository.EnrollRepository;

@Service
public class EnrollService {
    @Autowired private EnrollRepository enrollRepository;
}
