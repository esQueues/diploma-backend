package kz.sayat.diploma_backend.auth_module.dto;

import kz.sayat.diploma_backend.course_module.dto.CourseSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private int id;
    private String email;
    private String firstname;
    private String lastname;
    int gradeLevel;
    String schoolInfo;
    LocalDate birthday;
    List<CourseSummaryDto> enrolledCourses;
}
