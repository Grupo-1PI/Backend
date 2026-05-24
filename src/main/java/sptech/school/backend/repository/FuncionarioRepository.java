package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByCargoId(Long cargoId);
}
