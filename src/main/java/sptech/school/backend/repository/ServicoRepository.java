package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}