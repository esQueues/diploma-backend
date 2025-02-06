package kz.sayat.diploma_backend.controller;

import kz.sayat.diploma_backend.dto.LectureDto;
import kz.sayat.diploma_backend.models.Lecture;
import kz.sayat.diploma_backend.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses/modules")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/{moduleId}/lectures")
    public ResponseEntity<LectureDto> createLecture(@RequestBody LectureDto dto, @PathVariable int moduleId) {
        dto.setModuleId(moduleId);
        Lecture createdLecture = lectureService.createLecture(dto);
        dto.setId(createdLecture.getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/lectures/{id}")
    public ResponseEntity<LectureDto> getLecture(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(lectureService.findLectureById(id));
    }
}
