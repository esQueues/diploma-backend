package kz.sayat.diploma_backend.course_module.service.implementations;


import jakarta.transaction.Transactional;
import kz.sayat.diploma_backend.auth_module.dto.StudentDto;
import kz.sayat.diploma_backend.auth_module.mapper.StudentMapper;
import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.models.enums.UserRole;
import kz.sayat.diploma_backend.auth_module.repository.StudentRepository;
import kz.sayat.diploma_backend.auth_module.service.StudentService;
import kz.sayat.diploma_backend.auth_module.service.TeacherService;
import kz.sayat.diploma_backend.course_module.dto.CourseSummaryDto;
import kz.sayat.diploma_backend.course_module.service.CourseService;
import kz.sayat.diploma_backend.util.exceptions.ResourceNotFoundException;
import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.course_module.mapper.CourseMapper;
import kz.sayat.diploma_backend.course_module.repository.CourseRepository;
import kz.sayat.diploma_backend.auth_module.repository.TeacherRepository;
import kz.sayat.diploma_backend.auth_module.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper mapper;
    private final TeacherRepository teacherRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;


    @Override
    public CourseDto createCourse(CourseDto dto, Authentication authentication) {

        Course course = mapper.toCourse(dto);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Teacher teacher = teacherRepository.findById(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        course.setTeacher(teacher);

        return courseMapper.toCourseDto(courseRepository.save(course));
    }


    @Override
    public CourseDto findCourseById(int id, Authentication auth) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        boolean isEnrolled = false;
        boolean isTeacher = false;

        if (auth != null && auth.isAuthenticated()) {
            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
            UserRole role = userDetails.getUser().getRole();

            if (role == UserRole.STUDENT) {
                Student authenticatedStudent = studentService.getStudentFromUser(auth);
                isEnrolled = course.getStudents().stream()
                    .anyMatch(student -> student.getId() == authenticatedStudent.getId());
            } else if (role == UserRole.TEACHER) {
                Teacher authenticatedTeacher = teacherService.getTeacherFromUser(auth);
                isTeacher = (course.getTeacher().getId() == authenticatedTeacher.getId());
            }
        }

        CourseDto courseDto = mapper.toCourseDto(course);
        courseDto.setEnrolled(isEnrolled);
        courseDto.setCreator(isTeacher);

        return courseDto;
    }

    @Override
    public void enrollCourse(int courseId, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!(user instanceof Student student)) {
            throw new RuntimeException("User is not a student");
        }

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException( "Course not found"));

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentRepository.save(student);
        }
    }

    @Override
    public List<CourseSummaryDto> getAllCourses() {
        List<Course> courses=courseRepository.findAll();
        return courseMapper.toCourseSummaryDtoList(courses);
    }

    @Override
    public List<CourseSummaryDto> getMyCourses(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!(user instanceof Student student)) {
            throw new RuntimeException("User is not a student");
        }

        Student fetchedStudent = studentRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Course> courses = fetchedStudent.getCourses().stream()
            .filter(Course::isPublic)
            .collect(Collectors.toList());

        return courseMapper.toCourseSummaryDtoList(courses);
    }

    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<StudentDto> getStudentForCourse(int id) {
        Course course= courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return studentMapper.toStudentDtoList(course.getStudents());
    }

    @Override
    public List<CourseSummaryDto> getCourses(String search) {
        if (search != null && !search.isEmpty()) {
            List<Course> courses=courseRepository.findByTitleContainingIgnoreCaseAndIsPublicTrue(search);
            return courseMapper.toCourseSummaryDtoList(courses);
        }
        List<Course> courses=courseRepository.findByIsPublicTrue();
        return courseMapper.toCourseSummaryDtoList(courses);
    }

    @Override
    public void approve(int id) {
        Course course= courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setPublic(true);
    }

    @Override
    public List<CourseSummaryDto> getPrivateCourses() {
        List<Course> courses= courseRepository.findByIsPublicFalse();
        return courseMapper.toCourseSummaryDtoList(courses);
    }

    @Override
    public void disallow(int id) {
        Course course= courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setPublic(false);
    }

    @Override
    public void editCourse(CourseDto dto, int id) {
        Course course= courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
    }

}
