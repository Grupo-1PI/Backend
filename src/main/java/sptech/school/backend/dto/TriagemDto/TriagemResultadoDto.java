package sptech.school.backend.dto.TriagemDto;

public sealed interface TriagemResultadoDto permits TriagemResultadoDto.Continuar, TriagemResultadoDto.Finalizar {

    record Continuar(TriagemPerguntaResponseDto proxima) implements TriagemResultadoDto {}

    record Finalizar(EspecialidadeTriagemResponseDto especialidade) implements TriagemResultadoDto {}

    static TriagemResultadoDto continuar(TriagemPerguntaResponseDto proxima) {
        return new Continuar(proxima);
    }

    static TriagemResultadoDto finalizar(EspecialidadeTriagemResponseDto especialidade) {
        return new Finalizar(especialidade);
    }
}