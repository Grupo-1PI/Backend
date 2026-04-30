package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Agendamento;
import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByFuncionarioIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long funcionarioId,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    boolean existsBySalaIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long salaId,
            LocalDateTime fim,
            LocalDateTime inicio
    );
}