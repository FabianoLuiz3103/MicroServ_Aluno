package br.com.fiap.ms_aluno.repository;

import br.com.fiap.ms_aluno.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
