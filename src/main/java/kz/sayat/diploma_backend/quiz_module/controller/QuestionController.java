package kz.sayat.diploma_backend.quiz_module.controller;

import kz.sayat.diploma_backend.quiz_module.dto.QuestionDto;
import kz.sayat.diploma_backend.quiz_module.models.Question;
import kz.sayat.diploma_backend.quiz_module.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/{quizId}/question")
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto dto,
                                                   @PathVariable(name = "quizId") int quizId) {
        dto.setQuizId(quizId);
        Question savedQuestion = questionService.createQuestion(dto);
        dto.setId(savedQuestion.getId());
        savedQuestion.getAnswers().forEach(savedAnswer ->
            dto.getAnswers().stream()
                .filter(answerDto -> answerDto.getAnswerText().equals(savedAnswer.getAnswerText()))
                .findFirst()
                .ifPresent(answerDto -> answerDto.setId(savedAnswer.getId()))
        );
        return ResponseEntity.ok(dto);
    }


}
