package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.backend.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}