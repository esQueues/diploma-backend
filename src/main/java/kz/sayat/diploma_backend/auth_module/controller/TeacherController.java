package kz.sayat.diploma_backend.auth_module.controller;

import kz.sayat.diploma_backend.auth_module.dto.TeacherDto;
import kz.sayat.diploma_backend.auth_module.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/profile")
    public ResponseEntity<TeacherDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok().body(teacherService.getProfile(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable int id) {
        TeacherDto teacherDto = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacherDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(Authentication authentication, @RequestBody TeacherDto teacherDto) {
        TeacherDto updatedTeacher = teacherService.updateTeacher(authentication, teacherDto);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        List<TeacherDto> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

}
