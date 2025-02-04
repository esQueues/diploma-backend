package kz.sayat.diploma_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Teacher extends User{

    private String bio;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private List<Course> createdCourses;

}
