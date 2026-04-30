package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}

