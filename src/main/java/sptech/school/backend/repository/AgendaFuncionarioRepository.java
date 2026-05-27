package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.AgendaFuncionario;
import java.util.List;

public interface AgendaFuncionarioRepository extends JpaRepository<AgendaFuncionario, Long> {

    List<AgendaFuncionario> findByFuncionarioId(Long funcionarioId);

    void deleteByFuncionarioId(Long funcionarioId);
}
