package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.dto.CourseDto;
import kz.sayat.diploma_backend.models.Course;

public interface CourseMapper {
    Course toCourse(CourseDto courseDto);
    CourseDto toCourseDto(Course course);
}
