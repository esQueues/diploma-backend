package kz.sayat.diploma_backend.security.authReq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
    String email,

    String password,

    String firstname,

    String lastname
) {}
