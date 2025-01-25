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
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50)
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password is mandatory")
//    @Size(min = 4, max = 50,message = "Password have to be minimum 5 character")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "first_name")
    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50,message = "First name have to be minimum 2 character")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50,message = "Last name have to be minimum 2 character")
    private String lastName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
