package br.com.fiap.ms_aluno.service;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import br.com.fiap.ms_aluno.dto.DadosDetalhamentoAluno;
import br.com.fiap.ms_aluno.factory.Factory;
import br.com.fiap.ms_aluno.model.Aluno;
import br.com.fiap.ms_aluno.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class AlunoServiceTests {

    //Referencia AlunoService
    //@Autowired - Sem injeção de dependência
    //Mock - Injetando
    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    //Não vamos acessar o DB
    //Preparando os dados
    private Long existingId;
    private Long nonExistingId;

    //próximos testes
    private Aluno aluno;
    private DadosCadastroAluno alunoDTO;
    private DadosAtualizacaoAluno atualizacaoAlunoDTO;

    @BeforeEach
    void setup() throws Exception {

        existingId = 1L;
        nonExistingId = 10L;

        //precisa simular o comportamento do objeto mockado
        //delete - id existe
        Mockito.when(alunoRepository.existsById(existingId)).thenReturn(true);

        //delete - quando id não existe
        Mockito.when(alunoRepository.existsById(nonExistingId)).thenReturn(false);

        //delete - primeiro caso - deleta
        //não faã nada (void) quando...
        Mockito.doNothing().when(alunoRepository).deleteById(existingId);

        aluno = Factory.createAluno();
        alunoDTO = new DadosCadastroAluno(aluno);
        atualizacaoAlunoDTO =  new DadosAtualizacaoAluno(aluno);

        //Simulação do comportamento

        //findById
        Mockito.when(alunoRepository.findById(existingId)).thenReturn(Optional.of(aluno));
        Mockito.when(alunoRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //insert
        Mockito.when(alunoRepository.save(any())).thenReturn(aluno);

        //update - primeiro caso - id existe
        Mockito.when(alunoRepository.getReferenceById(existingId)).thenReturn(aluno);

        //updade - segundo caso - id não existe
        Mockito.when(alunoRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando id existe")
    public void deleteShouldDoNothingWhenIdExists(){
        //no service, delete é do tipo void
        Assertions.assertDoesNotThrow(
                () -> {
                    alunoService.delete(existingId);
                }
        );
    }

    @Test
    public void deleteShouldDoNothingWhenIdNotExists(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
           alunoService.delete(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWhitAutoIncrementWhenIdIsNull(){

        //Action
        var alunoDetalhes = alunoService.insert(alunoDTO);
        //Assertions
        Assertions.assertNotNull(alunoDetalhes);
        Assertions.assertEquals(alunoDetalhes.id(), aluno.getId());

    }


    @Test
    public void updateShoudIdExists(){

        //Action
        var alunoDetalhes = alunoService.updade(existingId, atualizacaoAlunoDTO);

        //Asserts
        Assertions.assertNotNull(alunoDetalhes);
        Assertions.assertEquals(alunoDetalhes.id(), existingId);
        Assertions.assertEquals(alunoDetalhes.nome(), aluno.getNome());
    }

    @Test
    public void updateShoudIdNotExists(){

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            alunoService.updade(nonExistingId, atualizacaoAlunoDTO);
        });
    }
}
