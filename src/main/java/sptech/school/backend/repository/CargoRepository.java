package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}