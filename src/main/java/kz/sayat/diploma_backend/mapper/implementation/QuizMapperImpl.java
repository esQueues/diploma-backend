package kz.sayat.diploma_backend.mapper.implementation;

import kz.sayat.diploma_backend.dto.QuizDto;
import kz.sayat.diploma_backend.mapper.QuizMapper;
import kz.sayat.diploma_backend.models.Quiz;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizMapperImpl implements QuizMapper {
    @Override
    public Quiz toQuiz(QuizDto dto) {
        if(dto == null) {
            return null;
        }
        Quiz quiz = new Quiz();
        quiz.setId(dto.getId());
        quiz.setTitle(dto.getTitle());
        return quiz;
    }

    @Override
    public QuizDto toQuizDto(Quiz quiz) {
        if(quiz == null) {
            return null;
        }
        QuizDto quizDto = new QuizDto();
        quizDto.setId(quiz.getId());
        quizDto.setTitle(quiz.getTitle());
        quizDto.setModuleId(quiz.getModule().getId());
        return quizDto;
    }

    @Override
    public List<QuizDto> toQuizDtoList(List<Quiz> quizList) {
        return quizList.stream().
            map(this::toQuizDto).
            collect(Collectors.toList());
    }
}
