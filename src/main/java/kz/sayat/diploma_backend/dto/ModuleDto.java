package kz.sayat.diploma_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ModuleDto {
    private int id;
    private String title;

    private int courseId;
    private List<QuizDto> quizzes;
    private List<LectureDto> lectures;
}

