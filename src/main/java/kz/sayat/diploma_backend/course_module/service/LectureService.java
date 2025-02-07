package kz.sayat.diploma_backend.course_module.service;

import kz.sayat.diploma_backend.course_module.dto.LectureDto;
import kz.sayat.diploma_backend.course_module.mapper.LectureMapper;
import kz.sayat.diploma_backend.course_module.models.Lecture;
import kz.sayat.diploma_backend.course_module.models.Module;
import kz.sayat.diploma_backend.course_module.repository.LectureRepository;
import kz.sayat.diploma_backend.course_module.repository.ModuleRepository;
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
