package Main.controller;

import Main.model.Aluno;
import Main.model.Usuario.Perfil;
import Main.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {
    
    @Autowired
    private AlunoService service;
    
    @GetMapping
    public ResponseEntity<List<Aluno>> listar(@RequestParam String perfil) {
        try {
            List<Aluno> alunos = service.listarTodos(Perfil.valueOf(perfil));
            return ResponseEntity.ok(alunos);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Aluno aluno, @RequestParam String perfil) {
        try {
            Aluno novoAluno = (Aluno) service.cadastrar(aluno, Perfil.valueOf(perfil));
            return ResponseEntity.ok(novoAluno);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Permissão negada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Aluno aluno, @RequestParam String perfil) {
        try {
            Aluno alunoEditado = (Aluno) service.editar(id, aluno, Perfil.valueOf(perfil));
            return ResponseEntity.ok(alunoEditado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Permissão negada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id, @RequestParam String perfil) {
        try {
            service.excluir(id, Perfil.valueOf(perfil));
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Permissão negada");
        }
    }
}