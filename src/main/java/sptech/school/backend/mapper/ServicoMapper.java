package sptech.school.backend.mapper;

import sptech.school.backend.dto.ServicoDto.ServicoRequestDto;
import sptech.school.backend.dto.ServicoDto.ServicoResponseDto;
import sptech.school.backend.entity.Sala;
import sptech.school.backend.entity.Servico;
import sptech.school.backend.repository.SalaRepository;

import java.util.ArrayList;
import java.util.List;

public class ServicoMapper {
    private final SalaRepository salaRepository;

    public ServicoMapper(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public Servico toEntity(ServicoRequestDto requestDto) {
        Servico servico = new Servico();
        List<Sala> salas = new ArrayList<>();
        requestDto.getSalasIds().forEach((salaId) -> {
            salaRepository.findById(salaId).ifPresent(salas::add);
        });
        servico.setNome(requestDto.getNome());
        servico.setValor(requestDto.getValor());
        servico.setDescricao(requestDto.getDescricao());
        servico.setTempoMedio(requestDto.getTempoMedio());
        servico.setSalas(salas);

        return servico;
    }

    public ServicoResponseDto toResponse(Servico servico) {
        return new ServicoResponseDto(
                servico.getId(),
                servico.getNome(),
                servico.getValor(),
                servico.getDescricao(),
                servico.getTempoMedio(),
                servico.getSalas()
        );
    }

    public List<ServicoResponseDto> toResponseList(List<Servico> servicos) {
        return servicos.stream()
                .map(this::toResponse)
                .toList();
    }
}