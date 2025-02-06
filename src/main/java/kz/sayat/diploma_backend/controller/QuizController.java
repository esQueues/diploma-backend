package kz.sayat.diploma_backend.controller;

import kz.sayat.diploma_backend.dto.QuizDto;
import kz.sayat.diploma_backend.models.Quiz;
import kz.sayat.diploma_backend.service.CourseService;
import kz.sayat.diploma_backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/courses/modules")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/{moduleId}/quizzes")
    private ResponseEntity<QuizDto> createQuiz(@RequestBody QuizDto dto,
                                                @PathVariable(name = "moduleId") int moduleId) {
        dto.setModuleId(moduleId);
        Quiz createdQuiz = quizService.createQuiz(dto);
        dto.setId(createdQuiz.getId());
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/quizzes/{quizId}")
    private ResponseEntity<QuizDto> getQuiz(@PathVariable(name = "quizId") int quizId) {
       return ResponseEntity.ok().body( quizService.findQuiz(quizId));
    }

}
