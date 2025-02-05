package kz.sayat.diploma_backend.dto;

import lombok.Data;

@Data
public class LectureDto {
    private int id;
    private String title;
    private String url;
    private int moduleId;
}
