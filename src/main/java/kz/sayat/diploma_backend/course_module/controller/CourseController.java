package kz.sayat.diploma_backend.course_module.controller;

import kz.sayat.diploma_backend.course_module.dto.CourseDto;
import kz.sayat.diploma_backend.course_module.models.Course;
import kz.sayat.diploma_backend.course_module.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable("id") int id) {
        return ResponseEntity .ok().body(courseService.findCourseById(id));
    }


}
