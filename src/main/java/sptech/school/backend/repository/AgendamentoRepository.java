package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto;
import sptech.school.backend.dto.DashboardDto.ServicoDadosDto;
import sptech.school.backend.entity.Agendamento;

import java.time.LocalDate;
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

    @Query("""
            SELECT COUNT(a.id)
            FROM Agendamento a
            WHERE a.dataHoraInicio >= :inicio
              AND a.dataHoraInicio <= :fim
        """)
    Integer findByTotalAgendamentos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            SELECT new sptech.school.backend.dto.DashboardDto.ServicoDadosDto(
                ats.servico.nome,
                COUNT(ats.id)
            )
            FROM AtendimentoServico ats
            WHERE ats.agendamento.dataHoraInicio >= :inicio
              AND ats.agendamento.dataHoraInicio <= :fim
            GROUP BY ats.servico.nome
    """)
    List<ServicoDadosDto> findByQuantidadeDeCadaServico(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT COUNT(DISTINCT a.cliente.id)
        FROM Agendamento a
        WHERE a.cliente.usuario.ativo = true
          AND a.dataHoraInicio >= :inicio
          AND a.dataHoraInicio <= :fim
    """)
    Integer findByClienteAtivosNoPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT COUNT(DISTINCT a.cliente.id)
        FROM Agendamento a
        WHERE a.cliente.usuario.ativo = true
          AND a.dataHoraInicio < :inicio
    """)
    Integer findByClientesNovosPeriodo(
            @Param("inicio") LocalDateTime inicio
    );

    @Query("""
        SELECT new sptech.school.backend.dto.DashboardDto.AgendamentoDiasSemanaDto(
            CASE FUNCTION('DAYOFWEEK', a.dataHoraInicio)
                WHEN 1 THEN 'Domingo'
                WHEN 2 THEN 'Segunda-feira'
                WHEN 3 THEN 'Terça-feira'
                WHEN 4 THEN 'Quarta-feira'
                WHEN 5 THEN 'Quinta-feira'
                WHEN 6 THEN 'Sexta-feira'
                WHEN 7 THEN 'Sábado'
            END,
            COUNT(a)
        )
        FROM Agendamento a
        WHERE a.dataHoraInicio BETWEEN :inicio AND :fim
        GROUP BY FUNCTION('DAYOFWEEK', a.dataHoraInicio)
    """)
    List<AgendamentoDiasSemanaDto> findByTotalAtendimentoDiaSemana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT COUNT(a)
        FROM Agendamento a
        WHERE a.status.id = 3
          AND a.dataHoraInicio >= :inicio
          AND a.dataHoraInicio <= :fim
    """)
    Integer findByTotalCancelamentos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
