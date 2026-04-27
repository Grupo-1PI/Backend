package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
