package kz.sayat.diploma_backend.mapper;

import kz.sayat.diploma_backend.dto.LectureDto;
import kz.sayat.diploma_backend.models.Lecture;

import java.util.List;

public interface LectureMapper {
    Lecture toLecture(LectureDto lectureDto);
    LectureDto toLectureDto(Lecture lecture);
    List<LectureDto> toLectureDtoList(List<Lecture> lectureList);
}
