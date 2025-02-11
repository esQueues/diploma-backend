package kz.sayat.diploma_backend.course_module.service;

import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseDto dto, Authentication authentication);
    CourseDto findCourseById(int id);
    void enrollCourse(int courseId, Authentication authentication);
    List<CourseDto> getMyCourses(Authentication authentication);
}
