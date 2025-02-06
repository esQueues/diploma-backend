package kz.sayat.diploma_backend.service;

import kz.sayat.diploma_backend.dto.QuizDto;
import kz.sayat.diploma_backend.mapper.QuizMapper;
import kz.sayat.diploma_backend.models.Module;
import kz.sayat.diploma_backend.models.Quiz;
import kz.sayat.diploma_backend.repository.ModuleRepository;
import kz.sayat.diploma_backend.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;
    private final QuizMapper mapper;


    public Quiz createQuiz(QuizDto dto) {
        Module module= moduleRepository.findById(dto.getModuleId()).
            orElseThrow(NoSuchElementException::new);
        Quiz quiz = mapper.toQuiz(dto);
        quiz.setModule(module);
        return quizRepository.save(quiz);

    }

    public QuizDto findQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).
            orElseThrow(NoSuchElementException::new);
        return mapper.toQuizDto(quiz);
    }

    public List<QuizDto> findAllQuizByModuleId(int moduleId) {
        List<Quiz >quizzes=quizRepository.findQuizzesByModule_Id(moduleId);
        return mapper.toQuizDtoList(quizzes);
    }
}
