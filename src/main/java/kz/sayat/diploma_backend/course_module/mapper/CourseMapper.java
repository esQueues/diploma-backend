package kz.sayat.diploma_backend.course_module.mapper;

import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.models.Course;

public interface CourseMapper {
    Course toCourse(CourseDto courseDto);
    CourseDto toCourseDto(Course course);
}
