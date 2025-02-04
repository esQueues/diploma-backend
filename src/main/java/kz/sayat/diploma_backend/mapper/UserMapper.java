package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.dto.UserDto;
import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;

public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
    User toUser(UserDto userDto);
    UserDto toUserDto(User user);
}
