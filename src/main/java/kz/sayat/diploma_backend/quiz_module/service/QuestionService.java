package kz.sayat.diploma_backend.quiz_module.service;

import kz.sayat.diploma_backend.quiz_module.dto.QuestionDto;
import kz.sayat.diploma_backend.quiz_module.mapper.QuestionMapper;
import kz.sayat.diploma_backend.quiz_module.models.Question;
import kz.sayat.diploma_backend.quiz_module.models.Quiz;
import kz.sayat.diploma_backend.quiz_module.repository.QuestionRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuizRepository quizRepository;

    @Transactional
    public QuestionDto createQuestion(QuestionDto dto, int quizId) {

        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = questionMapper.toQuestion(dto, quiz);
        questionRepository.save(question);

        return questionMapper.toDto(question);
    }

    @Transactional
    public List<QuestionDto> createQuestions(List<QuestionDto> dtos, int quizId) {

        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<Question> questions = questionMapper.toQuestionList(dtos, quiz);
        questionRepository.saveAll(questions);

        return questionMapper.toDtoList(questions);

    }

}
