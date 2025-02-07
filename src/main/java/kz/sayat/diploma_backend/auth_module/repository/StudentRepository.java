package kz.sayat.diploma_backend.auth_module.repository;

import kz.sayat.diploma_backend.auth_module.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
