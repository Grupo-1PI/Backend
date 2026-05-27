package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.backend.entity.FuncionarioAgendamento;
import sptech.school.backend.entity.FuncionarioAgendamentoId;
import java.time.LocalDateTime;
import java.util.List;

public interface FuncionarioAgendamentoRepository extends JpaRepository<FuncionarioAgendamento, FuncionarioAgendamentoId> {

    @Query("""
            select count(fa) > 0
            from FuncionarioAgendamento fa
            where fa.funcionario.id = :funcionarioId
              and fa.agendamento.dataHoraInicio < :fim
              and fa.agendamento.dataHoraFim > :inicio
              and (:ignorarId is null or :ignorarId = 0 or fa.agendamento.id <> :ignorarId)
            """)
    boolean existeConflitoFuncionario(
            @Param("funcionarioId") Long funcionarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("ignorarId") Long ignorarId
    );

    List<FuncionarioAgendamento> findByAgendamentoId(Long agendamentoId);
}
