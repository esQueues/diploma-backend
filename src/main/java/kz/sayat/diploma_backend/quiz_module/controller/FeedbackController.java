package kz.sayat.diploma_backend.quiz_module.controller;

import kz.sayat.diploma_backend.quiz_module.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/{attemptId}")
    public ResponseEntity<String> getQuizFeedback(@PathVariable(name = "attemptId") int attemptId ) {
        return ResponseEntity.status(201).body(feedbackService.generateFeedback(attemptId));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<String> getFeedback(@PathVariable(name = "attemptId") int attemptId,
                                              Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(feedbackService.getFeedbackOfStudent(attemptId,authentication));
    }

}
