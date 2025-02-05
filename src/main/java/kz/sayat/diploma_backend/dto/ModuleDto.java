package kz.sayat.diploma_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModuleDto {
    private int id;
    private String title;

    private int courseId;
}

