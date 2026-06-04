package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.AgendaExcecao;
import java.time.LocalDate;
import java.util.List;

public interface AgendaExcecaoRepository extends JpaRepository<AgendaExcecao, Long> {

    List<AgendaExcecao> findByFuncionarioIdAndData(Long funcionarioId, LocalDate data);

    List<AgendaExcecao> findByFuncionarioId(Long funcionarioId);
}
