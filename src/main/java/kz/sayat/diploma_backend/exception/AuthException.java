package kz.sayat.diploma_backend.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
