package kz.sayat.diploma_backend.course_module.repository;

import kz.sayat.diploma_backend.course_module.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByModule_Id(Integer moduleId);
}
