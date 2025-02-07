package kz.sayat.diploma_backend.quiz_module.repository;

import kz.sayat.diploma_backend.quiz_module.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    List<Quiz> findQuizzesByModule_Id(Integer moduleId);
}
