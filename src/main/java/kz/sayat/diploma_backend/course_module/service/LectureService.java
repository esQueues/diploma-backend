package kz.sayat.diploma_backend.course_module.service;

import kz.sayat.diploma_backend.course_module.dto.LectureDto;
import kz.sayat.diploma_backend.course_module.models.Lecture;

import java.util.List;

public interface LectureService {
    Lecture createLecture(LectureDto dto);
    LectureDto findLectureById(int id);
    List<LectureDto> findAllLecturesByModuleId(int moduleId);

}
