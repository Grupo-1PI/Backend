package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}