package kz.sayat.diploma_backend.quiz_module.service;

import kz.sayat.diploma_backend.quiz_module.models.*;
import kz.sayat.diploma_backend.quiz_module.repository.FeedbackRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final GeminiService geminiService;



    public String generateFeedback(int attemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(attemptId)
            .orElseThrow(() -> new NoSuchElementException("Quiz attempt not found for ID: " + attemptId));

        String promptText = buildPrompt(quizAttempt);
        String feedbackText=geminiService.getFeedback(promptText);

        feedbackRepository.save(new Feedback(promptText, feedbackText, quizAttempt));
        return feedbackText;
    }
    private String buildPrompt(QuizAttempt quizAttempt) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Тест әрекетінің қорытындысы:\n");
        prompt.append("Студент: ").append(quizAttempt.getStudent().getFirstname()).append("\n");
        prompt.append("Тест тақырыбы: ").append(quizAttempt.getQuiz().getTitle()).append("\n");
        prompt.append("Әрекет нөмірі: ").append(quizAttempt.getAttemptNumber()).append("\n");
        prompt.append("Ұпай: ").append(quizAttempt.getScore()).append("/100\n\n");

        prompt.append("Жауаптар:\n");
        for (QuizAttemptAnswer attemptAnswer : quizAttempt.getAttemptAnswers()) {
            Question question = attemptAnswer.getQuestion();

            String correctAnswerText = question.getAnswers().stream()
                .filter(Answer::isCorrect) // 'isCorrect' - дұрыс жауап екенін анықтайтын өріс
                .map(Answer::getAnswerText)
                .findFirst()
                .orElse("Дұрыс жауап табылмады");

            prompt.append("- Сұрақ: ").append(question.getQuestionText()).append("\n");
            prompt.append("  Сіздің жауабыңыз: ").append(attemptAnswer.getAnswer().getAnswerText()).append("\n");
            prompt.append("  Дұрыс жауап: ").append(correctAnswerText).append("\n");
            prompt.append("  Нәтиже: ").append(attemptAnswer.isCorrect() ? "✅ Дұрыс" : "❌ Қате").append("\n\n");

            prompt.append("  🧐 Бұл сұрақ бойынша егжей-тегжейлі талдау жасаңыз. Егер студенттің жауабы қате болса, қатенің себебін түсіндіріңіз және оны қалай болдырмау керектігін айтыңыз. Егер жауап дұрыс болса, қысқаша мақтау немесе қосымша түсініктеме беріңіз.\n\n");

        }

        prompt.append("Осы тест әрекеті бойынша егжей-тегжейлі қазақша кері байланыс беріңіз. Студенттің күшті жақтарын, жиі кездесетін қателерді және жақсарту жолдарын атап көрсетіңіз.");

        return prompt.toString();
    }

}
