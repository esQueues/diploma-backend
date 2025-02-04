package kz.sayat.diploma_backend.service;


import kz.sayat.diploma_backend.dto.CourseDto;
import kz.sayat.diploma_backend.mapper.CourseMapper;
import kz.sayat.diploma_backend.models.Course;
import kz.sayat.diploma_backend.models.Teacher;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.repository.CourseRepository;
import kz.sayat.diploma_backend.repository.TeacherRepository;
import kz.sayat.diploma_backend.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper mapper;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    public void createCourse(CourseDto dto, Authentication authentication) {
        Course course = mapper.toCourse(dto);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Teacher teacher = teacherRepository.findById(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        course.setTeacher(teacher);

        courseRepository.save(course);
    }

    public CourseDto findCourseById(int id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        return mapper.toCourseDto(course);
    }

}
