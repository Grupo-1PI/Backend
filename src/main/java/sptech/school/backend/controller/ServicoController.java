package sptech.school.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.ServicoDto.ServicoRequestDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.service.ServicoService;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {
    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listar() {
        return ResponseEntity.status(200).body(service.listar());
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody ServicoRequestDto requestDto) {
        service.salvar(requestDto);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity atualizarEntidade(@PathVariable Long id, @RequestBody ServicoRequestDto requestDto) {
        service.atualizarEntidade(id, requestDto);
        return ResponseEntity.status(200).build();
    }

    @PatchMapping
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody ServicoRequestDto requestDto) {
        service.atualizar(id, requestDto);
        return ResponseEntity.status(200).build();
    }
}
