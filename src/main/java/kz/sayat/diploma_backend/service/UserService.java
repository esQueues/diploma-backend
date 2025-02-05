package kz.sayat.diploma_backend.service;


import kz.sayat.diploma_backend.dto.UserDto;
import kz.sayat.diploma_backend.exceptions.UnauthorizedException;
import kz.sayat.diploma_backend.mapper.UserMapper;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.repository.UserRepository;
import kz.sayat.diploma_backend.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserDto getUserProfile(Authentication authentication) {
        if(!authentication.isAuthenticated()){
            throw new UnauthorizedException("User is not authenticated");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return  userMapper.toUserDto(user);
    }

}
