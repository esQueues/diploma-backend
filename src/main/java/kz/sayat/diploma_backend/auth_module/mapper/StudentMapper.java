package kz.sayat.diploma_backend.auth_module.mapper;

import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;
import kz.sayat.diploma_backend.auth_module.models.Student;

public interface StudentMapper {

    Student toStudent(RegisterRequest registerRequest);

}
