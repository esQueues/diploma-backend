package kz.sayat.diploma_backend.service;

import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.models.UserRole;
import kz.sayat.diploma_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

//    public void register(User user) {
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setRole(UserRole.STUDENT);
//        user.setCreatedAt(LocalDateTime.now());
//        userRepository.save(user);
//    }


}
