package kz.sayat.diploma_backend.quiz_module.controller;

import kz.sayat.diploma_backend.quiz_module.service.FeedbackService;
import kz.sayat.diploma_backend.quiz_module.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final GeminiService geminiService;

    @PostMapping("/{attemptId}")
    public ResponseEntity<String> getQuizFeedback(@PathVariable(name = "attemptId") int attemptId ) {

        String feedback = feedbackService.generateFeedback(attemptId);
        return ResponseEntity.ok(feedback);
    }

}
