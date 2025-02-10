package kz.sayat.diploma_backend.quiz_module.service;

import kz.sayat.diploma_backend.quiz_module.dto.QuestionDto;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    String generateFeedback(int attemptId);
    String getFeedbackOfStudent(int attemptId, Authentication authentication);
}
