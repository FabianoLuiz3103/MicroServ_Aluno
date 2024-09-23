package br.com.fiap.ms_aluno.repository;

import br.com.fiap.ms_aluno.factory.Factory;
import br.com.fiap.ms_aluno.model.Aluno;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class AlunoRepositoryTests {

    @Autowired
    private AlunoRepository alunoRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalAluno;

    //Executado antes de CADA teste
    @BeforeEach
    void setup() throws Exception{
        existingId = 1L;
        nonExistingId = 10L;
        countTotalAluno = 0L;
    }

    @Test
    @DisplayName("Deveria excluir o aluno quando o Id existe")
    public void deleteShouldDeletObjectWhenIdExists(){

        //Arrange - existingId

        //Action
        alunoRepository.deleteById(existingId);

        //Assert
        Optional<Aluno> result = alunoRepository.findById(existingId);

        //Testa se existe aluno dentro do Optional
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("save Deveria salvar um objeto com auto incremento de id quando o id é nulo")
    public void saveShouldPersistWhitAutoIncrementWhenIdIsNull(){

        //Action
        var aluno = Factory.createAluno();
        aluno.setId(null);
        alunoRepository.save(aluno);

        //Assert
        Assertions.assertNotNull(aluno.getId());
        Assertions.assertEquals(7L, aluno.getId());

    }


    @Test
    public void findByIdShouldIdDoesExists(){

        //Action
        var aluno = alunoRepository.findById(existingId);

        //Assert
        Assertions.assertTrue(aluno.isPresent());
    }

    @Test
    public void findByIdShouldIdDoesNotExists(){

        //Action
        var aluno = alunoRepository.findById(nonExistingId);

        //Assert
        Assertions.assertFalse(aluno.isPresent());
    }
}
