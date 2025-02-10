package kz.sayat.diploma_backend.quiz_module.controller;

import kz.sayat.diploma_backend.quiz_module.dto.QuizAttemptDto;
import kz.sayat.diploma_backend.quiz_module.dto.QuizDto;
import kz.sayat.diploma_backend.quiz_module.dto.StudentAnswerDto;
import kz.sayat.diploma_backend.quiz_module.models.QuizAttempt;
import kz.sayat.diploma_backend.quiz_module.models.Quiz;
import kz.sayat.diploma_backend.quiz_module.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/modules")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/{moduleId}/quizzes")
    public ResponseEntity<QuizDto> createQuiz(@RequestBody QuizDto dto,
                                                @PathVariable(name = "moduleId") int moduleId) {
        return ResponseEntity.status(201).body(quizService.createQuiz(dto, moduleId));
    }

    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable(name = "quizId") int quizId) {
       return ResponseEntity.ok().body( quizService.findQuiz(quizId));
    }


    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<QuizAttemptDto> submitQuiz(@PathVariable(name = "quizId") int quizId,
                                                  @RequestBody List<StudentAnswerDto> attemptAnswers,
                                                  Authentication authentication) {
        return ResponseEntity.ok(quizService.assignGrade(attemptAnswers, authentication, quizId));
    }

    @DeleteMapping("/quizzes/{quizId}")
    public void deleteQuiz(@PathVariable(name = "quizId") int quizId) {
        quizService.delete(quizId);
    }


}
