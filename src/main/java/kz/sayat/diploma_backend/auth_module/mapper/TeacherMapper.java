package kz.sayat.diploma_backend.auth_module.mapper;

import kz.sayat.diploma_backend.auth_module.dto.TeacherDto;
import kz.sayat.diploma_backend.auth_module.models.Teacher;
import kz.sayat.diploma_backend.auth_module.dto.RegisterRequest;

public interface TeacherMapper {
    Teacher toTeacher(TeacherDto teacherDto);
    TeacherDto toTeacherDto(Teacher teacher);
    Teacher toTeacher(RegisterRequest registerRequest);
}
