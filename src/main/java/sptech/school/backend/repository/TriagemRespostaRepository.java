package sptech.school.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sptech.school.backend.entity.TriagemPergunta;
import sptech.school.backend.entity.TriagemResposta;
import sptech.school.backend.entity.RespostaEnum;
import java.util.Optional;

public interface TriagemRespostaRepository extends JpaRepository<TriagemResposta, Long> {

    Optional<TriagemResposta> findByPerguntaIdAndResposta(Long perguntaId, RespostaEnum resposta);

    Optional<TriagemResposta> findByPerguntaAndResposta(TriagemPergunta pergunta, RespostaEnum resposta);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TriagemResposta r WHERE r.pergunta.id = :perguntaId")
    void deleteByPerguntaId(Long perguntaId);

    @Modifying
    @Query("UPDATE TriagemResposta r SET r.proximaPergunta = null WHERE r.proximaPergunta.id = :perguntaId")
    void limparReferenciasProximaPergunta(Long perguntaId);
}