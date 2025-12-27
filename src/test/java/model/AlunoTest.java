package model;

import Main.model.Aluno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Aluno")
class AlunoTest {
    
    @Test
    @DisplayName("Deve criar um aluno com todos os campos")
    void testCriacaoAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setRg("12.345.678-9");
        aluno.setCurso("Análise e Desenvolvimento de Sistemas");
        
        assertNotNull(aluno);
        assertEquals("João Silva", aluno.getNome());
        assertEquals("12.345.678-9", aluno.getRg());
        assertEquals("Análise e Desenvolvimento de Sistemas", aluno.getCurso());
    }
    
    @Test
    @DisplayName("Deve criar aluno usando setters")
    void testAlunoConstrutor() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Maria Santos");
        aluno.setRg("98.765.432-1");
        aluno.setCurso("Engenharia");

        assertEquals(1L, aluno.getId());
        assertEquals("Maria Santos", aluno.getNome());
        assertEquals("98.765.432-1", aluno.getRg());
        assertEquals("Engenharia", aluno.getCurso());
    }
    
    @Test
    @DisplayName("Deve permitir alterar dados do aluno")
    void testAlterarDadosAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Nome Inicial");
        aluno.setRg("11.111.111-1");
        aluno.setCurso("Curso Inicial");
        
        aluno.setNome("Nome Alterado");
        aluno.setRg("22.222.222-2");
        aluno.setCurso("Curso Alterado");
        
        assertEquals("Nome Alterado", aluno.getNome());
        assertEquals("22.222.222-2", aluno.getRg());
        assertEquals("Curso Alterado", aluno.getCurso());
    }
}