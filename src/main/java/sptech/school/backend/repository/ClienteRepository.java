package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Cliente;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioEmail(String email);

    Optional<Cliente> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}
