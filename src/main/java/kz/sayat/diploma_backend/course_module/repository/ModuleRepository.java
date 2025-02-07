package kz.sayat.diploma_backend.course_module.repository;

import kz.sayat.diploma_backend.course_module.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
