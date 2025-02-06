package kz.sayat.diploma_backend.service;


import kz.sayat.diploma_backend.dto.LectureDto;
import kz.sayat.diploma_backend.dto.ModuleDto;
import kz.sayat.diploma_backend.dto.QuizDto;
import kz.sayat.diploma_backend.mapper.ModuleMapper;
import kz.sayat.diploma_backend.mapper.implementation.ModuleMapperImpl;
import kz.sayat.diploma_backend.models.Course;
import kz.sayat.diploma_backend.models.Module;
import kz.sayat.diploma_backend.repository.CourseRepository;
import kz.sayat.diploma_backend.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper mapper;
    private final CourseRepository courseRepository;
    private final QuizService quizService;
    private final LectureService lectureService;

    public Module createModule(ModuleDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
            .orElseThrow(() -> new NoSuchElementException("Course with ID " + dto.getCourseId() + " not found"));
        Module module = mapper.toModule(dto);
        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public ModuleDto findModuleById(int id) {
        Module module= moduleRepository.findById(id).orElseThrow(()
            -> new NoSuchElementException("Module with ID " + id + " not found"));

        List<QuizDto> quizzes = quizService.findAllQuizByModuleId(id);
        List<LectureDto> lectures = lectureService.findAllLecturesByModuleId(id);

        ModuleDto dto = mapper.toModuleDto(module);
        dto.setQuizzes(quizzes);
        dto.setLectures(lectures);
        return dto;
    }
}
