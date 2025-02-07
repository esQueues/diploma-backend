//package kz.sayat.diploma_backend.service;
//
//import kz.sayat.diploma_backend.dto.AnswerDto;
//import kz.sayat.diploma_backend.quiz_module.mapper.AnswerMapper;
//import kz.sayat.diploma_backend.models.Answer;
//import kz.sayat.diploma_backend.repository.AnswerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AnswerService {
//
//    private final AnswerRepository answerRepository;
//    private final AnswerMapper answerMapper;
//
//    public List<AnswerDto> save(List<AnswerDto> answer) {
//        List<Answer> answers=answerMapper.toAnswerList(answer);
//
//        answerRepository.save(answer);
//        return answer;
//    }
//}
