package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.backend.entity.Agendamento;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("""
            select count(a) > 0
            from Agendamento a
            where a.sala.id = :salaId
              and a.dataHoraInicio < :fim
              and a.dataHoraFim > :inicio
              and (:ignorarId is null or :ignorarId = 0 or a.id <> :ignorarId)
            """)
    boolean existeConflitoSala(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("ignorarId") Long ignorarId
    );

    @Query("""
            select a
            from Agendamento a
            where a.dataHoraInicio < :fim
              and a.dataHoraFim > :inicio
            order by a.dataHoraInicio
            """)
    List<Agendamento> findByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    List<Agendamento> findByStatusId(Long statusId);

    List<Agendamento> findByClienteId(Long clienteId);
}
