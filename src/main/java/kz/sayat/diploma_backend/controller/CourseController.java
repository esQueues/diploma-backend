package kz.sayat.diploma_backend.controller;

import kz.sayat.diploma_backend.dto.CourseDto;
import kz.sayat.diploma_backend.models.Course;
import kz.sayat.diploma_backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping()
    public ResponseEntity<CourseDto> save(@RequestBody CourseDto courseDto, Authentication authentication) {
        System.out.println(courseDto);
        courseService.createCourse(courseDto,authentication);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable("id") int id) {
        return ResponseEntity .ok().body(courseService.findCourseById(id));
    }
}
