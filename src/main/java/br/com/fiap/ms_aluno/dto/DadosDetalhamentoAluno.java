package br.com.fiap.ms_aluno.dto;

import br.com.fiap.ms_aluno.model.Aluno;
import br.com.fiap.ms_aluno.model.Status;

public record DadosDetalhamentoAluno(

        Long id,
        String nome,
        String email,
        String password,
        String rm,
        Status status,
        String turma


) {
    public DadosDetalhamentoAluno(Aluno aluno){
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getPassword(),
                aluno.getRm(),
                aluno.getStatus(),
                aluno.getTurma()
        );
    }
}
