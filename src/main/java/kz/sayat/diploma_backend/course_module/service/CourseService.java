package kz.sayat.diploma_backend.course_module.service;

import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.dto.CourseSummaryDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course createCourse(CourseDto dto, Authentication authentication);
    CourseDto findCourseById(int id, Authentication auth);
    void enrollCourse(int courseId, Authentication authentication);
    List<CourseSummaryDto> getMyCourses(Authentication authentication);
    List<CourseDto> getAllCourses();

    void deleteCourse(int id);
}
