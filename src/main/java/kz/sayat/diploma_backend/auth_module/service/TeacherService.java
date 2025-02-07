package kz.sayat.diploma_backend.auth_module.service;

import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    public void save(Teacher teacher){
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
    }
}
