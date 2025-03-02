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
import kz.sayat.diploma_backend.course_module.dto.ModuleDto;
import kz.sayat.diploma_backend.course_module.repository.ModuleRepository;
import kz.sayat.diploma_backend.course_module.service.CourseService;
import kz.sayat.diploma_backend.quiz_module.models.Quiz;
import kz.sayat.diploma_backend.quiz_module.models.QuizAttempt;
import kz.sayat.diploma_backend.quiz_module.repository.QuizAttemptRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizRepository;
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
import kz.sayat.diploma_backend.course_module.models.Module;
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
    private final ModuleRepository moduleRepository;

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository attemptRepository;

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

        final Student authenticatedStudent; // Добавляем final
        boolean isEnrolled = false;
        boolean isTeacher = false;

        if (auth != null && auth.isAuthenticated()) {
            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
            UserRole role = userDetails.getUser().getRole();

            if (role == UserRole.STUDENT) {
                authenticatedStudent = studentService.getStudentFromUser(auth);
                isEnrolled = course.getStudents().stream()
                    .anyMatch(student -> student.getId() == authenticatedStudent.getId());
            } else if (role == UserRole.TEACHER) {
                Teacher authenticatedTeacher = teacherService.getTeacherFromUser(auth);
                isTeacher = (course.getTeacher().getId() == authenticatedTeacher.getId());
                authenticatedStudent = null; // Чтобы не было ошибки инициализации
            } else {
                authenticatedStudent = null;
            }
        } else {
            authenticatedStudent = null;
        }

        CourseDto courseDto = mapper.toCourseDto(course);
        courseDto.setEnrolled(isEnrolled);
        courseDto.setCreator(isTeacher);

        if (authenticatedStudent != null) {
            for (ModuleDto moduleDto : courseDto.getModules()) {
                double progress = calculateModuleProgress(authenticatedStudent.getId(), moduleDto.getId());
                moduleDto.setProgress(progress);
            }
        } else {
            courseDto.getModules().forEach(module -> module.setProgress(0));
        }

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
            .toList();

        return courses.stream()
            .map(course -> {
                CourseSummaryDto dto = courseMapper.toCourseSummaryDto(course);
                double progress = calculateCourseProgress(fetchedStudent.getId(), course.getId());
                dto.setProgress(progress);
                return dto;
            })
            .collect(Collectors.toList());
    }

    public double calculateCourseProgress(int studentId, int courseId) {
        // Получаем все модули курса
        List<Module> modules = moduleRepository.findByCourseId(courseId);

        double totalProgress = 0;
        int totalModules = modules.size();

        for (Module module : modules) {
            totalProgress += calculateModuleProgress(studentId, module.getId());
        }

        return totalModules == 0 ? 0 : totalProgress / totalModules;
    }

    public double calculateModuleProgress(int studentId, int moduleId) {
        int totalQuizzes = quizRepository.countByModuleId(moduleId);

        if (totalQuizzes == 0) {
            return 100.0;
        }

        List<Quiz> quizzes = quizRepository.findQuizzesByModule_Id(moduleId);

        double totalScore = 0;

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        for (Quiz quiz : quizzes) {
            QuizAttempt lastAttempt = attemptRepository.findTopByStudentAndQuizOrderByAttemptNumberDesc(student, quiz);

            if (lastAttempt != null && lastAttempt.isPassed()) {
                totalScore += lastAttempt.getScore();
            }
        }

        return totalScore / totalQuizzes;
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
