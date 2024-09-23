package br.com.fiap.ms_aluno.factory;

import br.com.fiap.ms_aluno.model.Aluno;
import br.com.fiap.ms_aluno.model.Status;

// Classe para instanciar objetos
public class Factory {

    public static Aluno createAluno(){

        return new Aluno(
                1L,
                "Fabiano Luiz",
                "fabianoluiz123@gmail.com",
                "12345678",
                "96044",
                Status.MATRICULADO,
                "3SIPG"
        );
    }
}
