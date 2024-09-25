package br.com.fiap.ms_aluno.controller;

import br.com.fiap.ms_aluno.dto.DadosAtualizacaoAluno;
import br.com.fiap.ms_aluno.dto.DadosCadastroAluno;
import br.com.fiap.ms_aluno.dto.DadosDetalhamentoAluno;
import br.com.fiap.ms_aluno.factory.Factory;
import br.com.fiap.ms_aluno.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTests {

    @Autowired
    private MockMvc mockMvc; //para chamar o endpoint
    //controller tem dependência do service
    //dependência mockada
    @MockBean
    private AlunoService alunoService;
    private DadosCadastroAluno dadosCadastroAluno;
    private DadosDetalhamentoAluno dadosDetalhamentoAluno;
    private DadosAtualizacaoAluno dadosAtualizacaoAluno;
    private Long existingId;
    private Long nonExistingId;
    //Converter para JSON o objeto Java e enviar na requisição
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception{

        existingId = 1L;
        nonExistingId = 12L;

        //criando um DADOS DE CADASTRO DE ALUNO
        dadosCadastroAluno = Factory.createDadosCadastroAluno();

        //criando um DADOS DETALHAMENTO ALUNO
        dadosDetalhamentoAluno = Factory.createDadosDetalhamentoAluno();

        //criando um DADOS ATUALIZACAO ALUNO
        dadosAtualizacaoAluno = Factory.createDadosAtualizacaoAluno();

        //Listando
        List<DadosDetalhamentoAluno> list = List.of(dadosDetalhamentoAluno);

        //simulando comportamento do service - findAll
        when(alunoService.findAll()).thenReturn(list);

        //simulando comportamento do service - findById - id existe
        when(alunoService.findById(existingId)).thenReturn(dadosDetalhamentoAluno);

        //simulando comportamento do service - findById - id não existe
        when(alunoService.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //simulando comportamento do service - insert
        //any() simula o comportamento de qualquer objeto
        when(alunoService.insert(any())).thenReturn(dadosDetalhamentoAluno);

        //simulando o comportamento do service - update
        //any() simula o comportamento de qualquer objeto
        //quando usamos o any(), não pode usar objetos simples, então usamos eq()
        //primeiro caso - id existe
        when(alunoService.updade(eq(existingId), any())).thenReturn(dadosDetalhamentoAluno);

        //segundo caso - id não existe
        when(alunoService.updade(eq(nonExistingId), any())).thenThrow(EntityNotFoundException.class);

        //delete - id existe
        doNothing().when(alunoService).delete(existingId);

        //delete - id não existe
        doThrow(IllegalArgumentException.class).when(alunoService).delete(nonExistingId);
        doThrow(EntityNotFoundException.class).when(alunoService).delete(nonExistingId);




    }

    @Test
    public void findAllShouldReturnListPagamentoDTO() throws Exception{

        //chamando uma requisição com o método get em /alunos
        ResultActions resultActions = mockMvc.perform(get("/alunos")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnAlunoWhenIdExists() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/alunos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        //Assertions
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.nome").exists());
        resultActions.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void findByIdShouldReturNotFoundWhenIdDoesNotExists() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/alunos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        //Assertions
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnAlunoCreated() throws Exception{

        var dadosCadastroAluno = Factory.createDadosCadastroAluno();
        var json = objectMapper.writeValueAsString(dadosCadastroAluno);

        mockMvc.perform(post("/alunos")
                .content(json) //requisição
                .contentType(MediaType.APPLICATION_JSON) //requisção
                .accept(MediaType.APPLICATION_JSON)//resposta
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.status").exists());

    }

    @Test
    public void updateShouldReturnAlunoWhenIdExists() throws Exception{

        //PUT tem corpo - JSON
        //precisamos passar o corpo da requisição
        //Bean objectMapper para converter Java para Json
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
                .andExpect(jsonPath("$.status").exists());
    }


    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        //PUT tem corpo - JSON
        //precisamos passar o corpo da requisição
        //Bean objectMapper para converter Java para Json
        var json = objectMapper.writeValueAsString(dadosAtualizacaoAluno);

        mockMvc.perform(put("/alunos{id}", existingId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
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
    public void deleteShouldReturnNotFoundNWhenIdDoesNotExists() throws Exception {

        mockMvc.perform(delete("/alunos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}
