package kz.sayat.diploma_backend.auth_module.mapper.implementation;

import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import kz.sayat.diploma_backend.auth_module.mapper.StudentMapper;
import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.models.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {
    @Override
    public Student toStudent(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }
        Student student = new Student();
        student.setEmail(registerRequest.email());
        student.setPassword(registerRequest.password());
        student.setFirstname(registerRequest.firstname());
        student.setLastname(registerRequest.lastname());
        student.setGradeLevel(registerRequest.gradeLevel());
        student.setBirthDate(registerRequest.birthDate());
        student.setSchoolInfo(registerRequest.schoolInfo());
        student.setRole(UserRole.STUDENT);
        return student;
    }
}
