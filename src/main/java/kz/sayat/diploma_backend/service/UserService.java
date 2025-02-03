package kz.sayat.diploma_backend.service;


import kz.sayat.diploma_backend.dto.UserDto;
import kz.sayat.diploma_backend.exception.UnAuthException;
import kz.sayat.diploma_backend.mapper.UserMapper;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.repository.UserRepository;
import kz.sayat.diploma_backend.security.MyUserDetails;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public UserDto getUserProfile(Authentication authentication) {
        if(!authentication.isAuthenticated()){
            throw new UnAuthException("User is not authenticated");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser(); // Получаем объект User из MyUserDetails

        return  userMapper.toUserDto(user);
    }




}
