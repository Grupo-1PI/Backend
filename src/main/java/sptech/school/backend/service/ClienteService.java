package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import sptech.school.backend.entity.Cliente;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.repository.ClienteRepository;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado"));
    }
}
