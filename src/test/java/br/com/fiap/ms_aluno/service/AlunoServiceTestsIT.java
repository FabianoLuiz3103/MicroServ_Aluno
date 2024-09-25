package br.com.fiap.ms_aluno.service;

import br.com.fiap.ms_aluno.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest //carrega o contexto da aplicação
@Transactional //rollback no DB
public class AlunoServiceTestsIT {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private AlunoRepository alunoRepository;

    //preparando os dados
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalAlunos;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 50L;
        countTotalAlunos = 6L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){
        alunoService.delete(existingId);
        Assertions.assertEquals(countTotalAlunos - 1, alunoRepository.count());
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            alunoService.delete(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnListAluno(){

        var result = alunoService.findAll();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(countTotalAlunos, result.size());
        Assertions.assertEquals("Fabiano Luiz", result.get(0).nome());
        Assertions.assertEquals("93044", result.get(3).rm());
    }


}
