package kz.sayat.diploma_backend.auth_module.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
