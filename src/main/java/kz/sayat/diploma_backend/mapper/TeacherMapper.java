package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.dto.TeacherDto;
import kz.sayat.diploma_backend.models.Teacher;
import kz.sayat.diploma_backend.security.authReq.RegisterRequest;

public interface TeacherMapper {
    Teacher toTeacher(TeacherDto teacherDto);
    TeacherDto toTeacherDto(Teacher teacher);
    Teacher toTeacher(RegisterRequest registerRequest);
}
