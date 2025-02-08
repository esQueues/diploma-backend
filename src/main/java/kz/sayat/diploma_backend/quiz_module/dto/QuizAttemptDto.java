package kz.sayat.diploma_backend.quiz_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class QuizAttemptDto {
    private int attemptId;
    private int studentId;
    private int quizId;
    private int attemptNumber;
    private double score;
}
