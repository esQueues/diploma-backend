package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.dto.QuizDto;
import kz.sayat.diploma_backend.models.Quiz;

import java.util.List;

public interface QuizMapper {
    Quiz toQuiz(QuizDto dto);
    QuizDto toQuizDto(Quiz quiz);
    List<QuizDto> toQuizDtoList(List<Quiz> quizList);
}
