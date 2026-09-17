package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import sptech.school.backend.entity.Funcionario;
import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @EntityGraph(attributePaths = {"cargo", "cargo.permissoes"})
    Optional<Funcionario> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
    boolean existsByCargoId(Long cargoId);
    List<Funcionario> findByEspecialidadesId(Long especialidadeId);
}
