package sptech.school.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.backend.dto.TriagemDto.EspecialidadeTriagemResponseDto;
import sptech.school.backend.dto.TriagemDto.TriagemPerguntaCriacaoDto;
import sptech.school.backend.dto.TriagemDto.TriagemPerguntaResponseDto;
import sptech.school.backend.dto.TriagemDto.TriagemRespostaCriacaoDto;
import sptech.school.backend.dto.TriagemDto.TriagemResultadoDto;
import sptech.school.backend.entity.Especialidade;
import sptech.school.backend.entity.RespostaEnum;
import sptech.school.backend.entity.TriagemPergunta;
import sptech.school.backend.entity.TriagemResposta;
import sptech.school.backend.exception.RecursoNaoEncontradoException;
import sptech.school.backend.mapper.TriagemMapper;
import sptech.school.backend.repository.EspecialidadeRepository;
import sptech.school.backend.repository.TriagemPerguntaRepository;
import sptech.school.backend.repository.TriagemRespostaRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TriagemService {

    private final TriagemPerguntaRepository perguntaRepository;
    private final TriagemRespostaRepository respostaRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public TriagemService(
            TriagemPerguntaRepository perguntaRepository,
            TriagemRespostaRepository respostaRepository,
            EspecialidadeRepository especialidadeRepository
    ) {
        this.perguntaRepository = perguntaRepository;
        this.respostaRepository = respostaRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    @Transactional(readOnly = true)
    public TriagemPerguntaResponseDto iniciar() {
        long raizCount = perguntaRepository.countByRaizTrue();
        if (raizCount == 0) {
            throw new IllegalStateException("Nenhuma pergunta raiz cadastrada");
        }
        if (raizCount > 1) {
            throw new IllegalStateException("Mais de uma pergunta raiz cadastrada (deve haver apenas uma)");
        }
        TriagemPergunta raiz = perguntaRepository.findByRaizTrue()
                .orElseThrow(() -> new IllegalStateException("Nenhuma pergunta raiz cadastrada"));
        return TriagemMapper.toPerguntaResponse(raiz);
    }

    @Transactional(readOnly = true)
    public TriagemResultadoDto responder(Long perguntaId, RespostaEnum resposta) {
        TriagemResposta tr = respostaRepository.findByPerguntaIdAndResposta(perguntaId, resposta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Resposta inválida para esta pergunta"));

        if (tr.getProximaPergunta() != null) {
            return TriagemResultadoDto.continuar(TriagemMapper.toPerguntaResponse(tr.getProximaPergunta()));
        } else {
            Especialidade especialidade = tr.getEspecialidade();
            if (especialidade == null) {
                throw new IllegalStateException("Fim do ramo sem especialidade definida");
            }
            return TriagemResultadoDto.finalizar(TriagemMapper.toEspecialidadeResponse(especialidade));
        }
    }

    // ===== CRUD ADMIN =====

    @Transactional(readOnly = true)
    public List<TriagemPerguntaResponseDto> listarTodas() {
        return perguntaRepository.findAll().stream()
                .map(TriagemMapper::toPerguntaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TriagemPerguntaResponseDto buscarPorId(Long id) {
        TriagemPergunta p = perguntaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pergunta não encontrada"));
        return TriagemMapper.toPerguntaResponse(p);
    }

    @Transactional
    public TriagemPerguntaResponseDto criar(TriagemPerguntaCriacaoDto dto) {
        validarCriacao(dto);
        TriagemPergunta pergunta = new TriagemPergunta();
        pergunta.setTexto(dto.getTexto());
        pergunta.setOrdem(dto.getOrdem());
        pergunta.setRaiz(dto.getRaiz());
        if (dto.getEspecialidadeId() != null) {
            Especialidade esp = especialidadeRepository.findById(dto.getEspecialidadeId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
            pergunta.setEspecialidade(esp);
        }
        TriagemPergunta salva = perguntaRepository.save(pergunta);
        if (dto.getRespostas() != null) {
            for (TriagemRespostaCriacaoDto r : dto.getRespostas()) {
                TriagemResposta resposta = new TriagemResposta();
                resposta.setPergunta(salva);
                resposta.setResposta(r.getResposta());
                if (r.getProximaPerguntaId() != null) {
                    TriagemPergunta prox = perguntaRepository.findById(r.getProximaPerguntaId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Próxima pergunta não encontrada"));
                    resposta.setProximaPergunta(prox);
                }
                if (r.getEspecialidadeId() != null) {
                    Especialidade esp = especialidadeRepository.findById(r.getEspecialidadeId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
                    resposta.setEspecialidade(esp);
                }
                respostaRepository.save(resposta);
            }
        }
        return TriagemMapper.toPerguntaResponse(salva);
    }

    @Transactional
    public TriagemPerguntaResponseDto atualizar(Long id, TriagemPerguntaCriacaoDto dto) {
        TriagemPergunta pergunta = perguntaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pergunta não encontrada"));

        if (dto.getRaiz() && !pergunta.getRaiz()) {
            long raizCount = perguntaRepository.countByRaizTrue();
            if (raizCount > 0) {
                throw new IllegalStateException("Já existe uma pergunta raiz");
            }
        }

        // Validar DTO antes de mexer no banco
        validarAtualizacao(dto, id);

        pergunta.setTexto(dto.getTexto());
        pergunta.setOrdem(dto.getOrdem());
        pergunta.setRaiz(dto.getRaiz());
        if (dto.getEspecialidadeId() != null) {
            Especialidade esp = especialidadeRepository.findById(dto.getEspecialidadeId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
            pergunta.setEspecialidade(esp);
        } else {
            pergunta.setEspecialidade(null);
        }

        // Atualiza respostas existentes (não deleta/recria)
        if (dto.getRespostas() != null) {
            for (TriagemRespostaCriacaoDto r : dto.getRespostas()) {
                TriagemResposta resposta = respostaRepository.findByPerguntaIdAndResposta(id, r.getResposta())
                        .orElseGet(() -> {
                            TriagemResposta nova = new TriagemResposta();
                            nova.setPergunta(pergunta);
                            nova.setResposta(r.getResposta());
                            return nova;
                        });

                resposta.setPergunta(pergunta);
                resposta.setResposta(r.getResposta());

                if (r.getProximaPerguntaId() != null) {
                    TriagemPergunta prox = perguntaRepository.findById(r.getProximaPerguntaId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Próxima pergunta não encontrada (ID: " + r.getProximaPerguntaId() + ")"));
                    resposta.setProximaPergunta(prox);
                } else {
                    resposta.setProximaPergunta(null);
                }

                if (r.getEspecialidadeId() != null) {
                    Especialidade esp = especialidadeRepository.findById(r.getEspecialidadeId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada (ID: " + r.getEspecialidadeId() + ")"));
                    resposta.setEspecialidade(esp);
                } else {
                    resposta.setEspecialidade(null);
                }

                respostaRepository.save(resposta);
            }
        }

        return TriagemMapper.toPerguntaResponse(perguntaRepository.save(pergunta));
    }

    private void validarAtualizacao(TriagemPerguntaCriacaoDto dto, Long perguntaId) {
        if (dto.getRaiz()) {
            long raizCount = perguntaRepository.countByRaizTrue();
            // Se já é raiz, não conta ela mesma
            boolean jaEhRaiz = perguntaRepository.findById(perguntaId)
                    .map(p -> p.getRaiz() != null && p.getRaiz())
                    .orElse(false);
            if (!jaEhRaiz && raizCount > 0) {
                throw new IllegalStateException("Já existe uma pergunta raiz");
            }
        }
        if (dto.getRespostas() == null || dto.getRespostas().size() != 2) {
            throw new IllegalArgumentException("Cada pergunta deve ter exatamente 2 respostas (SIM e NAO)");
        }
        long simCount = dto.getRespostas().stream().filter(r -> r.getResposta() == RespostaEnum.SIM).count();
        long naoCount = dto.getRespostas().stream().filter(r -> r.getResposta() == RespostaEnum.NAO).count();
        if (simCount != 1 || naoCount != 1) {
            throw new IllegalArgumentException("Deve haver exatamente uma resposta SIM e uma NAO");
        }
        for (TriagemRespostaCriacaoDto r : dto.getRespostas()) {
            boolean temProxima = r.getProximaPerguntaId() != null;
            boolean temEsp = r.getEspecialidadeId() != null;
            if (temProxima == temEsp) {
                throw new IllegalArgumentException("Resposta deve ter próximaPerguntaId OU especialidadeId (não ambos, nem nenhum)");
            }
            if (temProxima && r.getProximaPerguntaId().equals(perguntaId)) {
                throw new IllegalArgumentException("Uma resposta não pode apontar para a própria pergunta (ciclo)");
            }
        }
    }

    @Transactional
    public void deletar(Long id) {
        TriagemPergunta pergunta = perguntaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pergunta não encontrada"));
        
        // Primeiro: limpa referências de outras perguntas que apontam para esta
        respostaRepository.limparReferenciasProximaPergunta(id);
        
        // Segundo: remove as respostas desta pergunta
        respostaRepository.deleteByPerguntaId(id);
        
        // Terceiro: remove a pergunta
        perguntaRepository.delete(pergunta);
    }

    private void validarCriacao(TriagemPerguntaCriacaoDto dto) {
        if (dto.getRaiz()) {
            long raizCount = perguntaRepository.countByRaizTrue();
            if (raizCount > 0) {
                throw new IllegalStateException("Já existe uma pergunta raiz cadastrada");
            }
        }
        if (dto.getRespostas() == null || dto.getRespostas().size() != 2) {
            throw new IllegalArgumentException("Cada pergunta deve ter exatamente 2 respostas (SIM e NAO)");
        }
        long simCount = dto.getRespostas().stream().filter(r -> r.getResposta() == RespostaEnum.SIM).count();
        long naoCount = dto.getRespostas().stream().filter(r -> r.getResposta() == RespostaEnum.NAO).count();
        if (simCount != 1 || naoCount != 1) {
            throw new IllegalArgumentException("Deve haver exatamente uma resposta SIM e uma NAO");
        }
        for (TriagemRespostaCriacaoDto r : dto.getRespostas()) {
            boolean temProxima = r.getProximaPerguntaId() != null;
            boolean temEsp = r.getEspecialidadeId() != null;
            if (temProxima == temEsp) {
                throw new IllegalArgumentException("Resposta deve ter próximaPerguntaId OU especialidadeId (não ambos, nem nenhum)");
            }
        }
    }
}