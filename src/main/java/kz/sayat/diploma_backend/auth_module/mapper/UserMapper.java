package kz.sayat.diploma_backend.auth_module.mapper;

import kz.sayat.diploma_backend.auth_module.dto.UserDto;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;

public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
    User toUser(UserDto userDto);
    UserDto toUserDto(User user);
}
