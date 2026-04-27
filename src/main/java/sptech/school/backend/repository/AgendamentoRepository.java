package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Agendamento;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByDataHora(LocalDateTime dataHora);

    boolean existsByDataHoraAndIdNot(LocalDateTime dataHora, Long id);
}

