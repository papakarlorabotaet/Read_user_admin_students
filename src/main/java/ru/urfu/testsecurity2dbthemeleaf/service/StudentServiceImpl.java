package ru.urfu.testsecurity2dbthemeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.testsecurity2dbthemeleaf.entity.Student;
import ru.urfu.testsecurity2dbthemeleaf.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    public StudentRepository studentRepository;
    @Override
    public String getNameStudent(Long studentId) {
        Student student = studentRepository.getReferenceById(studentId);
        return student.getName();
    }
}
