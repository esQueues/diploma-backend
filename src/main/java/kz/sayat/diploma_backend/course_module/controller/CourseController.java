package kz.sayat.diploma_backend.course_module.controller;

import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.dto.CourseSummaryDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import kz.sayat.diploma_backend.course_module.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping()
    public ResponseEntity<CourseDto> courseCreation(@RequestBody CourseDto dto, Authentication authentication) {
        Course createdCourse= courseService.createCourse(dto, authentication);
        dto.setId(createdCourse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable int id,  Authentication auth) {
        CourseDto courseDto = courseService.findCourseById(id, auth);
        return ResponseEntity.ok(courseDto);
    }


    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollCourse(@PathVariable("courseId") int courseId, Authentication authentication) {
        courseService.enrollCourse(courseId,authentication);
        return ResponseEntity.ok("Student enrolled to course!");
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseSummaryDto>> getCoursesOfStudent(Authentication authentication) {
        return ResponseEntity.ok(courseService.getMyCourses(authentication));
    }

}
