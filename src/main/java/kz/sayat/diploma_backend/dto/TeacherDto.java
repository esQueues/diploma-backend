package kz.sayat.diploma_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherDto {
    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private String bio;
    private List<CourseDto> createdCourses;
}
