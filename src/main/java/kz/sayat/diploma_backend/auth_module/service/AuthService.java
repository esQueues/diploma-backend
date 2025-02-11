package kz.sayat.diploma_backend.auth_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.sayat.diploma_backend.auth_module.dto.LoginRequest;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;

public interface AuthService {
    void login(HttpServletRequest request, HttpServletResponse response, LoginRequest authRequest);
    void register(RegisterRequest request);
    void registerTeacher(RegisterRequest request);

}
