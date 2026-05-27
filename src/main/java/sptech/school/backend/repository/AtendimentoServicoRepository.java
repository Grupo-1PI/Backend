package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.AtendimentoServico;
import java.util.List;

public interface AtendimentoServicoRepository extends JpaRepository<AtendimentoServico, Long> {

    List<AtendimentoServico> findByAgendamentoId(Long agendamentoId);
}
