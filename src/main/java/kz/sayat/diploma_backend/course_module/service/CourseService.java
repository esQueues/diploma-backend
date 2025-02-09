package kz.sayat.diploma_backend.course_module.service;


import jakarta.transaction.Transactional;
import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.repository.StudentRepository;
import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.course_module.mapper.CourseMapper;


import kz.sayat.diploma_backend.course_module.repository.CourseRepository;
import kz.sayat.diploma_backend.auth_module.repository.TeacherRepository;
import kz.sayat.diploma_backend.auth_module.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper mapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    public Course createCourse(CourseDto dto, Authentication authentication) {
        Course course = mapper.toCourse(dto);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Teacher teacher = teacherRepository.findById(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        course.setTeacher(teacher);

        return courseRepository.save(course);
    }



    public CourseDto findCourseById(int id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        return mapper.toCourseDto(course);
    }

    @Transactional
    public void enrollCourse(int courseId, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!(user instanceof Student student)) {
            throw new RuntimeException("User is not a student");
        }

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentRepository.save(student);
        }
    }

}
