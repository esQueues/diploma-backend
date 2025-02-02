package kz.sayat.diploma_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
