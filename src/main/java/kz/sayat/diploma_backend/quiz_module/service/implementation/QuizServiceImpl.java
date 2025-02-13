package kz.sayat.diploma_backend.quiz_module.service.implementation;

import jakarta.transaction.Transactional;
import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.service.StudentService;
import kz.sayat.diploma_backend.auth_module.service.implementation.StudentServiceImpl;
import kz.sayat.diploma_backend.quiz_module.service.QuizService;
import kz.sayat.diploma_backend.util.exceptions.ResourceNotFoundException;
import kz.sayat.diploma_backend.course_module.dto.QuizSummaryDto;
import kz.sayat.diploma_backend.quiz_module.dto.QuizAttemptDto;
import kz.sayat.diploma_backend.quiz_module.dto.QuizDto;
import kz.sayat.diploma_backend.quiz_module.dto.StudentAnswerDto;
import kz.sayat.diploma_backend.quiz_module.mapper.QuizAttemptMapper;
import kz.sayat.diploma_backend.quiz_module.mapper.QuizMapper;
import kz.sayat.diploma_backend.course_module.models.Module;
import kz.sayat.diploma_backend.quiz_module.models.*;
import kz.sayat.diploma_backend.course_module.repository.ModuleRepository;
import kz.sayat.diploma_backend.quiz_module.repository.AttemptAnswerRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizAttemptRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final QuizAttemptMapper quizAttemptMapper;
    private final StudentService studentService;
    private final QuizMapper quizMapper;



    public QuizDto createQuiz(QuizDto dto, int moduleId) {

        Module module = moduleRepository.findById(moduleId).
            orElseThrow(() -> new ResourceNotFoundException("module not found"));
        Quiz quiz = quizMapper.toQuiz(dto, module);

        quizRepository.save(quiz);

        return quizMapper.toQuizDto(quiz);
    }

    public QuizDto findQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).
            orElseThrow(() -> new ResourceNotFoundException("quiz not found"));

        return quizMapper.toQuizDto(quiz);
    }

    public List<QuizSummaryDto> findAllQuizByModuleId(int moduleId) {
        List<Quiz >quizzes=quizRepository.findQuizzesByModule_Id(moduleId);
        return quizMapper.toQuizSummaryDtoList(quizzes);
    }

    public QuizAttemptDto assignGrade(List<StudentAnswerDto> studentAnswers, Authentication authentication, int quizId) {
        Student student= studentService.getStudentFromUser(authentication);

        Quiz quiz = quizRepository.findById(quizId).
            orElseThrow(() -> new ResourceNotFoundException("quiz not found"));

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setStudent(student);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setAttemptNumber(getNextAttemptNumber(student, quiz));

        List<QuizAttemptAnswer> attemptAnswers = mapStudentAnswersToAttempt(studentAnswers, quiz);

        quizAttempt.setScore(calculateScore(attemptAnswers, quiz));
        quizAttemptRepository.save(quizAttempt);

        for (QuizAttemptAnswer attemptAnswer : attemptAnswers) {
            attemptAnswer.setQuizAttempt(quizAttempt);
            attemptAnswerRepository.save(attemptAnswer);
        }

        return quizAttemptMapper.toQuizAttemptDto(quizAttempt);
    }

    public void delete(int quizId) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("quiz not found"));
        quizRepository.delete(quiz);
    }

    @Override
    public QuizAttemptDto getAttempt(int quizId, Authentication authentication) {
        Student student = studentService.getStudentFromUser(authentication);
        Quiz quiz= quizRepository.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("quiz not found"));
        QuizAttempt attempt = quizAttemptRepository
            .findTopByStudentAndQuizOrderByAttemptNumberDesc(student, quiz);
        return quizAttemptMapper.toQuizAttemptDto(attempt);
    }

    private int getNextAttemptNumber(Student student, Quiz quiz) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByStudentAndQuiz(student, quiz);
        return attempts.size() + 1;
    }

    private double calculateScore(List<QuizAttemptAnswer> attemptAnswers, Quiz quiz) {
        Map<Integer, Boolean> correctAnswersMap = quiz.getQuestions().stream()
            .flatMap(q -> q.getAnswers().stream())
            .collect(Collectors.toMap(Answer::getId, Answer::isCorrect));

        long totalQuestions = quiz.getQuestions().size();
        long correctAnswers = attemptAnswers.stream()
            .filter(attemptAnswer -> correctAnswersMap.get(attemptAnswer.getAnswer().getId()))
            .count();

        return (double) correctAnswers / totalQuestions * 100;
    }

    private List<QuizAttemptAnswer> mapStudentAnswersToAttempt(List<StudentAnswerDto> studentAnswers, Quiz quiz) {
        return studentAnswers.stream()
            .map(studentAnswer -> {
                Question question = quiz.getQuestions().stream()
                    .filter(q -> q.getId() == studentAnswer.getQuestionId())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

                Answer answer = question.getAnswers().stream()
                    .filter(a -> a.getId() == studentAnswer.getAnswerId())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

                return createAttemptAnswer(question, answer);
            })
            .collect(Collectors.toList());
    }
    private QuizAttemptAnswer createAttemptAnswer(Question question, Answer answer) {
        QuizAttemptAnswer attemptAnswer = new QuizAttemptAnswer();
        attemptAnswer.setQuestion(question);
        attemptAnswer.setAnswer(answer);
        attemptAnswer.setCorrect(answer.isCorrect());
        return attemptAnswer;
    }


}
