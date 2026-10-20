package sptech.school.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /** Quantos agendamentos (qualquer status) ocupam a sala informada. */
    long countBySalaId(Long salaId);

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

    @Query("""
            select a
            from Agendamento a
            where a.dataHoraInicio < :fim
              and a.dataHoraFim > :inicio
              and (:statusId is null or a.status.id = :statusId)
            """)
    Page<Agendamento> findByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusId") Long statusId,
            Pageable pageable
    );

    List<Agendamento> findByStatusId(Long statusId);

    Page<Agendamento> findByStatusId(Long statusId, Pageable pageable);

    List<Agendamento> findByClienteId(Long clienteId);

    @Query("""
            SELECT COUNT(a.id)
            FROM Agendamento a
            WHERE a.dataHoraInicio >= :inicio
              AND a.dataHoraInicio < :fim
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
              AND ats.agendamento.dataHoraInicio < :fim
              AND ats.agendamento.status.id = 4
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
          AND a.dataHoraInicio < :fim
          AND a.status.id <> 3
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
    SELECT COUNT(DISTINCT a.cliente.id)
    FROM Agendamento a
    WHERE a.status.id = 4
      AND a.dataHoraInicio >= :inicio
      AND a.dataHoraInicio < :fim
      AND NOT EXISTS (
          SELECT a2.id
          FROM Agendamento a2
          WHERE a2.cliente.id = a.cliente.id
            AND a2.status.id = 4
            AND a2.dataHoraInicio < :inicio
      )
""")
    Long findByNovosPacientes(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
    SELECT a.cliente.id, a.dataHoraInicio
    FROM Agendamento a
    WHERE a.status.id = 4
      AND a.dataHoraInicio >= :inicio
      AND a.dataHoraInicio < :fim
""")
    List<Object[]> findByAtendimentosFinalizadosNoPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query(value = """
        SELECT DAYOFWEEK(data_hora_inicio) AS dia_semana, COUNT(*) AS total
        FROM agendamento
        WHERE data_hora_inicio >= :inicio
          AND data_hora_inicio < :fim
        GROUP BY DAYOFWEEK(data_hora_inicio)
    """, nativeQuery = true)
    List<AgendamentoDiasSemanaDto> findByTotalAtendimentoDiaSemana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
    SELECT a.cliente.id, MIN(a.dataHoraInicio)
    FROM Agendamento a
    WHERE a.status.id = 4
      AND a.dataHoraInicio < :fim
    GROUP BY a.cliente.id
""")
    List<Object[]> findByPrimeiroAtendimentoFinalizado(
            @Param("fim") LocalDateTime fim
    );



    @Query("""
        SELECT COUNT(a)
        FROM Agendamento a
        WHERE a.status.id = 3
          AND a.dataHoraInicio >= :inicio
          AND a.dataHoraInicio < :fim
    """)
    Integer findByTotalCancelamentos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
