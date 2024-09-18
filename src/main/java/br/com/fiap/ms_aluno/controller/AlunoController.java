package br.com.fiap.ms_aluno.controller;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import br.com.fiap.ms_aluno.dto.DadosDetalhamentoAluno;
import br.com.fiap.ms_aluno.service.AlunoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAluno> insert(@RequestBody @Valid DadosCadastroAluno cadastroAluno,
                                                         UriComponentsBuilder uriBuilder){
        var alunoDTO = alunoService.insert(cadastroAluno);
        var uri = uriBuilder.path("/alunos/{id}").buildAndExpand(alunoDTO.id()).toUri();
        return ResponseEntity.created(uri).body(alunoDTO);
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoAluno>> findAll(){
        var alunosDTO = alunoService.findAll();
        return ResponseEntity.ok(alunosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAluno> findById(@PathVariable Long id){
        var alunoDTO = alunoService.findById(id);
        return ResponseEntity.ok(alunoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAluno> update(@PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoAluno atualizacaoAluno){
        var alunoDTO = alunoService.updade(id, atualizacaoAluno);
        return ResponseEntity.ok(alunoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
