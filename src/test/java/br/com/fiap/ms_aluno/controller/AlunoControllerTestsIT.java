package br.com.fiap.ms_aluno.controller;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import br.com.fiap.ms_aluno.dto.DadosDetalhamentoAluno;
import br.com.fiap.ms_aluno.factory.Factory;
import br.com.fiap.ms_aluno.model.Aluno;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional //rollback DB
public class AlunoControllerTestsIT {

    @Autowired
    private MockMvc mockMvc;
    //preparando os dados
    private Long existingId;
    private Long nonExistingId;
    private DadosCadastroAluno dadosCadastroAluno;
    private DadosDetalhamentoAluno dadosDetalhamentoAluno;
    private DadosAtualizacaoAluno dadosAtualizacaoAluno;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 50L;
        dadosCadastroAluno = Factory.createDadosCadastroAluno();
        dadosAtualizacaoAluno = Factory.createDadosAtualizacaoAluno();

    }

    @Test
    public void findAllShouldReturnListAllAlunos() throws Exception {

        mockMvc.perform(get("/alunos")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].nome").isString())
                .andExpect(jsonPath("[0].nome").value("Fabiano Luiz"));

    }

    @Test
    public void findByIdShouldReturnAlunoWhenIdExistis() throws Exception {

        mockMvc.perform(get("/alunos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("nome").isString())
                .andExpect(jsonPath("nome").value("Fabiano Luiz"));

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistis() throws Exception {

        mockMvc.perform(get("/alunos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
               // .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void insertShouldReturnAluno() throws Exception {

        var entity = Factory.createAluno();
        entity.setId(null);
        String json = objectMapper.writeValueAsString(entity);

        mockMvc.perform(post("/alunos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.nome").value("Fabiano Luiz"));
    }

    @Test
    public void insertShouldThrowsExceptionWhenInvalidData() throws Exception {
        var aluno = new Aluno();
        aluno.setNome("Aluno example");
        aluno.setRm("11111");

        var json = objectMapper.writeValueAsString(aluno);

        mockMvc.perform(post("/alunos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void updateShouldUpdateAndReturnAlunoWhenIdExists() throws Exception {

        var json = objectMapper.writeValueAsString(dadosAtualizacaoAluno);
        mockMvc.perform(put("/alunos/{id}", existingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        var json = objectMapper.writeValueAsString(dadosAtualizacaoAluno);

        mockMvc.perform(put("/alunos/{id}", nonExistingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        mockMvc.perform(delete("/alunos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        mockMvc.perform(delete("/alunos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}
