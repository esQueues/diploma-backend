package kz.sayat.diploma_backend.auth_module.mapper.implementation;


import kz.sayat.diploma_backend.auth_module.dto.UserDto;
import kz.sayat.diploma_backend.auth_module.mapper.UserMapper;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.auth_module.models.enums.UserRole;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return null;
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(registerRequest.password());
        user.setFirstname(registerRequest.firstname());
        user.setLastname(registerRequest.lastname());
        user.setRole(UserRole.STUDENT);
        // Set other fields as necessary
        return user;
    }

    @Override
    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        // Set other fields as necessary
        return user;
    }

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        // Set other fields as necessary
        return userDto;
    }
}
