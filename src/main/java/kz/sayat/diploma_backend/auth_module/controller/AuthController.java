package kz.sayat.diploma_backend.auth_module.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kz.sayat.diploma_backend.auth_module.dto.LoginRequest;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import kz.sayat.diploma_backend.auth_module.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest authRequest) {
        authService.login(request, response, authRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/register/teacher")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTeacher(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.registerTeacher(registerRequest);
    }

}






