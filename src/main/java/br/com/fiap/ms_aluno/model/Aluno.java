package br.com.fiap.ms_aluno.model;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})

@Entity
@Table(name="tbl_aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String turma;


    public Aluno(DadosCadastroAluno cadastroAluno){
        this.nome = cadastroAluno.nome();
        this.email = cadastroAluno.email();
        this.password = cadastroAluno.password();
        this.rm = cadastroAluno.rm();
        this.turma = cadastroAluno.turma();
    }

    public void update(DadosAtualizacaoAluno atualizacaoAluno){
        if(atualizacaoAluno.nome() != null){
            this.nome = atualizacaoAluno.nome();
        }
        if(atualizacaoAluno.email() != null){
            this.email = atualizacaoAluno.email();
        }
        if(atualizacaoAluno.password() != null){
            this.password = atualizacaoAluno.password();
        }
        if(atualizacaoAluno.rm() != null){
            this.rm = atualizacaoAluno.rm();
        }
        if(atualizacaoAluno.status() != null){
            this.status = atualizacaoAluno.status();
        }
        if(atualizacaoAluno.turma() != null){
            this.turma = atualizacaoAluno.turma();
        }
    }
}
