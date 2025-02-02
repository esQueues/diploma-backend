package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.models.User;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
}
