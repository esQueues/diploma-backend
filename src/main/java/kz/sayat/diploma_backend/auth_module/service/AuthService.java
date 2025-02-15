package kz.sayat.diploma_backend.auth_module.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.sayat.diploma_backend.auth_module.dto.LoginRequest;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import kz.sayat.diploma_backend.auth_module.dto.UserDto;
import kz.sayat.diploma_backend.auth_module.models.enums.UserRole;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthService {
    UserDto login(HttpServletRequest request, HttpServletResponse response, LoginRequest authRequest);
    void register(RegisterRequest request);
    void registerTeacher(RegisterRequest request);

    Map<String, String> giveRole(Authentication authentication);
}
