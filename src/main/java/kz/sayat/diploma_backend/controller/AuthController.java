package kz.sayat.diploma_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kz.sayat.diploma_backend.security.authReq.LoginRequest;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import kz.sayat.diploma_backend.service.AuthService;
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
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest authRequest) {
        authService.login(request, response, authRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("RegisterRequest: " + registerRequest.email());

        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}






