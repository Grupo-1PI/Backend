package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Status;
import sptech.school.backend.repository.StatusRepository;
import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> listar() {
        return statusRepository.findAll();
    }
}
