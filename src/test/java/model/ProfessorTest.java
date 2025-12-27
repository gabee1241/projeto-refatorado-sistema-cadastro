package model;

import Main.model.Professor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Professor")
class ProfessorTest {
    
    @Test
    @DisplayName("Deve criar um professor com todos os campos")
    void testCriacaoProfessor() {
        Professor professor = new Professor();
        professor.setNome("Dr. Carlos");
        professor.setRg("11.222.333-4");
        professor.setDepartamento("Computação");
        
        assertNotNull(professor);
        assertEquals("Dr. Carlos", professor.getNome());
        assertEquals("11.222.333-4", professor.getRg());
        assertEquals("Computação", professor.getDepartamento());
    }
    
    @Test
    @DisplayName("Deve criar professor usando setters")
    void testProfessorConstrutor() {
        Professor professor = new Professor();
        professor.setId(1L);
        professor.setNome("Dra. Ana");
        professor.setRg("44.555.666-7");
        professor.setDepartamento("Matemática");
        
        assertEquals(1L, professor.getId());
        assertEquals("Dra. Ana", professor.getNome());
        assertEquals("44.555.666-7", professor.getRg());
        assertEquals("Matemática", professor.getDepartamento());
    }
    
    @Test
    @DisplayName("Deve permitir alterar dados do professor")
    void testAlterarDadosProfessor() {
        Professor professor = new Professor();
        professor.setNome("Nome Inicial");
        professor.setRg("11.111.111-1");
        professor.setDepartamento("Depto Inicial");
        
        professor.setNome("Nome Alterado");
        professor.setRg("22.222.222-2");
        professor.setDepartamento("Depto Alterado");
        
        assertEquals("Nome Alterado", professor.getNome());
        assertEquals("22.222.222-2", professor.getRg());
        assertEquals("Depto Alterado", professor.getDepartamento());
    }
}
