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


    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizAttemptDto> submitQuiz(@PathVariable(name = "quizId") int quizId,
                                                  @RequestBody List<StudentAnswerDto> attemptAnswers,
                                                  Authentication authentication) {
        QuizAttempt quizAttempt = quizService.assignGrade(attemptAnswers, authentication, quizId);

        QuizAttemptDto quizAttemptDto = new QuizAttemptDto(
            quizAttempt.getId(),
            quizAttempt.getStudent().getId(),
            quizAttempt.getQuiz().getId(),
            quizAttempt.getAttemptNumber(),
            quizAttempt.getScore()
        );

        return ResponseEntity.ok(quizAttemptDto);
    }





}
