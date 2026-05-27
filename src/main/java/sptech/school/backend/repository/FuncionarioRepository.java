package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Funcionario;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
    boolean existsByCargoId(Long cargoId);
}
