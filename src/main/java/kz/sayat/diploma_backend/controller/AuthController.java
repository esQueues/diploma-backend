package kz.sayat.diploma_backend.controller;

import jakarta.validation.Valid;
import kz.sayat.diploma_backend.exceptions.UserRegException;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {
        try {
            userService.register(user);
        } catch (UserRegException e) {
            throw new UserRegException("Failed to register user: " + e.getMessage(), e); // Retain the original cause
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = userService.verify(user);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An error occurred during login"));
        }
    }


    @GetMapping("/main")
    public String mainPage() {
        return "Hello to the main page!";
    }

}
