package kz.sayat.diploma_backend.controller;

import kz.sayat.diploma_backend.dto.UserDto;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import kz.sayat.diploma_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok().body(userService.getUserProfile(authentication));
    }


}
