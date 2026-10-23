package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.TriagemPergunta;
import java.util.Optional;

public interface TriagemPerguntaRepository extends JpaRepository<TriagemPergunta, Long> {

    Optional<TriagemPergunta> findByRaizTrue();

    Optional<TriagemPergunta> findByIdAndRaizTrue(Long id);

    long countByRaizTrue();
}