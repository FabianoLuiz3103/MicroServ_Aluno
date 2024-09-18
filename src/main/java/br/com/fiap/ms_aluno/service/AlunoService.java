package br.com.fiap.ms_aluno.service;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import br.com.fiap.ms_aluno.dto.DadosDetalhamentoAluno;
import br.com.fiap.ms_aluno.model.Aluno;
import br.com.fiap.ms_aluno.model.Status;
import br.com.fiap.ms_aluno.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional
    public DadosDetalhamentoAluno insert(DadosCadastroAluno cadastroAluno){
        var aluno = new Aluno(cadastroAluno);
        aluno.setStatus(Status.MATRICULADO);
        alunoRepository.save(aluno);
        return new DadosDetalhamentoAluno(aluno);
    }

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoAluno> findAll(){
        var alunos = alunoRepository.findAll();
        return alunos.stream().map(DadosDetalhamentoAluno::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno findById(Long id){
        var aluno = alunoRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Aluno não encontrado com id: " + id)
                );
        return new DadosDetalhamentoAluno(aluno);
    }

    @Transactional
    public DadosDetalhamentoAluno updade(Long id, DadosAtualizacaoAluno atualizacaoAluno){
        try {
            var aluno = alunoRepository.getReferenceById(id);
            aluno.update(atualizacaoAluno);
            alunoRepository.save(aluno);
            return new DadosDetalhamentoAluno(aluno);
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Aluno não encontrado com id: " + id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!alunoRepository.existsById(id)){
            throw  new IllegalArgumentException("User não encontrado para o id:" + id);
        }
        try{
            alunoRepository.deleteById(id);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User não encontrado com id: " + id);
        }
    }
}
