package kz.sayat.diploma_backend.auth_module.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.sayat.diploma_backend.course_module.models.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Student extends User{

    private LocalDate birthDate;

    private Integer gradeLevel;

    private String schoolInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "enrollments",
        joinColumns = @JoinColumn(name = "student_id"),  // Correct order matching SQL
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

}
