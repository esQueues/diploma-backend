package kz.sayat.diploma_backend.service;

import kz.sayat.diploma_backend.dto.LectureDto;
import kz.sayat.diploma_backend.dto.ModuleDto;
import kz.sayat.diploma_backend.mapper.LectureMapper;
import kz.sayat.diploma_backend.models.Course;
import kz.sayat.diploma_backend.models.Lecture;
import kz.sayat.diploma_backend.models.Module;
import kz.sayat.diploma_backend.repository.LectureRepository;
import kz.sayat.diploma_backend.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final ModuleRepository moduleRepository;
    private final LectureMapper mapper;


    public Lecture createLecture(LectureDto dto) {
        Module module = moduleRepository.findById(dto.getModuleId()).
            orElseThrow(NoSuchElementException::new);
        Lecture lecture = mapper.toLecture(dto);
        lecture.setModule(module);
        return lectureRepository.save(lecture);
    }

    public LectureDto findLectureById(int id) {
        return mapper.toLectureDto(lectureRepository.findById(id)
            .orElse(null));
    }

    public List<LectureDto> findAllLecturesByModuleId(int moduleId) {

        List<Lecture> lectures = lectureRepository.findByModule_Id(moduleId);
        return mapper.toLectureDtoList(lectures);
    }


//
//    public Lecture findLectureById(int id) {
//        return lectureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lecture not found"));
//    }
}
