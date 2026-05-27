package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}
