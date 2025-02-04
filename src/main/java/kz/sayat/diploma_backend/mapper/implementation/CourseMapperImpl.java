package kz.sayat.diploma_backend.mapper.implementation;
import kz.sayat.diploma_backend.dto.CourseDto;
import kz.sayat.diploma_backend.mapper.CourseMapper;
import kz.sayat.diploma_backend.models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toCourse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }

        Course course = new Course();
        course.setId(courseDto.getId());
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());

        return course;
    }

    @Override
    public CourseDto toCourseDto(Course course) {
        if (course == null) {
            return null;
        }

        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setDescription(course.getDescription());

        return courseDto;
    }

}
