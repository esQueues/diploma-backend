package kz.sayat.diploma_backend.service;

import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.models.UserRole;
import kz.sayat.diploma_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public String verify(User user) {
        Authentication authentication =
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        return "fail";
    }


}
