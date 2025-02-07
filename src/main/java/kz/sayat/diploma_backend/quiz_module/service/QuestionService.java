package kz.sayat.diploma_backend.quiz_module.service;

import jakarta.transaction.Transactional;
import kz.sayat.diploma_backend.quiz_module.dto.QuestionDto;
import kz.sayat.diploma_backend.quiz_module.mapper.AnswerMapper;
import kz.sayat.diploma_backend.quiz_module.models.Answer;
import kz.sayat.diploma_backend.quiz_module.models.Question;
import kz.sayat.diploma_backend.quiz_module.models.Quiz;
import kz.sayat.diploma_backend.quiz_module.repository.QuestionRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
//    private final AnswerService answerService;
    private final AnswerMapper answerMapper;
    private final QuizRepository quizRepository;

    @Transactional
    public Question createQuestion(QuestionDto dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setQuiz(quiz);

        List<Answer> answers = answerMapper.toAnswerList(dto.getAnswers());
        answers.forEach(answer -> answer.setQuestion(question));
        question.setAnswers(answers);

        return questionRepository.save(question);
    }
}
