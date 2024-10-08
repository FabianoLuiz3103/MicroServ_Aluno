package br.com.fiap.ms_aluno.dto;

import br.com.fiap.ms_aluno.model.Aluno;
import br.com.fiap.ms_aluno.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoAluno(

        @NotBlank(message = "O nome é obrigatório! ")
        String nome,

        @NotBlank(message = "O email é obrigatório! ")
        @Email(message = "Formato de e-mail inválido!")
        String email,

        @NotBlank(message = "A senha é obrigatória! ")
        String password,

        @NotBlank(message = "O RM é obrigatório! ")
        @Size(min = 5, max = 5)
        String rm,

        @NotNull(message = "O status é obrigatório")
        Status status,

        @NotBlank(message = "A turma é obrgatório! ")
        String turma
) {

        public DadosAtualizacaoAluno(Aluno aluno){
                this(
                        aluno.getNome(),
                        aluno.getEmail(),
                        aluno.getPassword(),
                        aluno.getRm(),
                        aluno.getStatus(),
                        aluno.getTurma()
                );
        }
}
