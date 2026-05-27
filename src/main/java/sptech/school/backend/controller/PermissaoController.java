package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.backend.entity.Permissao;
import sptech.school.backend.repository.PermissaoRepository;
import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

    private final PermissaoRepository permissaoRepository;

    public PermissaoController(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Permissao>> listar() {
        return ResponseEntity.ok(permissaoRepository.findAll());
    }
}
