package kz.sayat.diploma_backend.quiz_module.repository;

import kz.sayat.diploma_backend.quiz_module.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
