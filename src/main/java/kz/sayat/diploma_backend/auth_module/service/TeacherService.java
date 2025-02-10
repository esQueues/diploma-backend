package kz.sayat.diploma_backend.auth_module.service;

import jakarta.transaction.Transactional;
import kz.sayat.diploma_backend.auth_module.dto.TeacherDto;
import kz.sayat.diploma_backend.util.exceptions.ResourceNotFoundException;
import kz.sayat.diploma_backend.util.exceptions.UnauthorizedException;
import kz.sayat.diploma_backend.auth_module.mapper.TeacherMapper;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.auth_module.repository.TeacherRepository;
import kz.sayat.diploma_backend.auth_module.models.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void save(Teacher teacher){
        teacher.setPassword(encoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
    }

    public TeacherDto getProfile(Authentication authentication) {
        return  teacherMapper.toTeacherDto(getTeacherFromUser(authentication));
    }

    public TeacherDto updateTeacher(Authentication authentication, TeacherDto teacherDto) {
        Teacher teacher = getTeacherFromUser(authentication);
        teacher.setBio(teacherDto.getBio());
        teacher.setEmail(teacherDto.getEmail());
        teacher.setFirstname(teacherDto.getFirstname());
        teacher.setLastname(teacherDto.getLastname());
        Teacher savedTeacher = teacherRepository.save(teacher);

        return teacherMapper.toTeacherDto(savedTeacher);
    }


    public void deleteTeacher(int id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        teacherRepository.delete(teacher);
    }

    public TeacherDto getTeacherById(int id) {
        Teacher teacher=teacherRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return teacherMapper.toTeacherDto(teacher);
    }

    public List<TeacherDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teacherMapper.toTeacherDtoList(teachers);
    }

    public Teacher getTeacherFromUser(Authentication authentication){
        if(!authentication.isAuthenticated()){
            throw new UnauthorizedException("User is not authenticated");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!(user instanceof Teacher teacher)) {
            throw new RuntimeException("User is not a student");
        }
        return teacher;
    }
}
