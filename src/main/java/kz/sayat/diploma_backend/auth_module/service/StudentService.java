package kz.sayat.diploma_backend.auth_module.service;

import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void save(Student student) {
        student.setPassword(encoder.encode(student.getPassword()));
        studentRepository.save(student);
    }

}
