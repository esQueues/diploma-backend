package kz.sayat.diploma_backend.quiz_module.service;

import kz.sayat.diploma_backend.auth_module.models.Student;
import kz.sayat.diploma_backend.auth_module.models.User;
import kz.sayat.diploma_backend.auth_module.repository.StudentRepository;
import kz.sayat.diploma_backend.auth_module.security.MyUserDetails;
import kz.sayat.diploma_backend.course_module.dto.QuizSummaryDto;
import kz.sayat.diploma_backend.quiz_module.dto.QuizDto;
import kz.sayat.diploma_backend.quiz_module.dto.StudentAnswerDto;
import kz.sayat.diploma_backend.quiz_module.mapper.QuizMapper;
import kz.sayat.diploma_backend.course_module.models.Module;
import kz.sayat.diploma_backend.quiz_module.models.*;
import kz.sayat.diploma_backend.course_module.repository.ModuleRepository;
import kz.sayat.diploma_backend.quiz_module.repository.AttemptAnswerRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizAttemptRepository;
import kz.sayat.diploma_backend.quiz_module.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final StudentRepository studentRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;

    private final QuizMapper mapper;


    public Quiz createQuiz(QuizDto dto) {
        Module module= moduleRepository.findById(dto.getModuleId()).
            orElseThrow(NoSuchElementException::new);
        Quiz quiz = mapper.toQuiz(dto);
        quiz.setModule(module);
        return quizRepository.save(quiz);

    }

    public QuizDto findQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).
            orElseThrow(NoSuchElementException::new);

        return mapper.toQuizDto(quiz);
    }

    public List<QuizSummaryDto> findAllQuizByModuleId(int moduleId) {
        List<Quiz >quizzes=quizRepository.findQuizzesByModule_Id(moduleId);
        return mapper.toQuizSummaryDtoList(quizzes);
    }

    public QuizAttempt assignGrade(List<StudentAnswerDto> studentAnswers, Authentication authentication, int quizId) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!(user instanceof Student student)) {
            throw new RuntimeException("User is not a student");
        }
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setStudent(student);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setAttemptNumber(getNextAttemptNumber(student, quiz));

        List<QuizAttemptAnswer> attemptAnswers = studentAnswers.stream().map(studentAnswer -> {
            Question question = quiz.getQuestions().stream()
                .filter(q -> q.getId() == studentAnswer.getQuestionId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer answer = question.getAnswers().stream()
                .filter(a -> a.getId() == studentAnswer.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Answer not found"));

            boolean isCorrect = answer.isCorrect();

            QuizAttemptAnswer attemptAnswer = new QuizAttemptAnswer();
            attemptAnswer.setQuestion(question);
            attemptAnswer.setAnswer(answer);
            attemptAnswer.setCorrect(isCorrect);
            return attemptAnswer;
        }).collect(Collectors.toList());

        quizAttempt.setScore(calculateScore(attemptAnswers, quiz));
        quizAttemptRepository.save(quizAttempt);

        for (QuizAttemptAnswer attemptAnswer : attemptAnswers) {
            attemptAnswer.setQuizAttempt(quizAttempt);
            attemptAnswerRepository.save(attemptAnswer);
        }
        return quizAttempt;
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

}
