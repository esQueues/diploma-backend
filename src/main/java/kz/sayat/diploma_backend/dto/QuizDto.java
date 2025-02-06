package kz.sayat.diploma_backend.dto;

import lombok.Data;

@Data
public class QuizDto {
    private int id;
    private String title;
    private int moduleId;
}
