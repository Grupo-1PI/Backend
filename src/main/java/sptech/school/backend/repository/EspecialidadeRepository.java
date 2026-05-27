package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}
