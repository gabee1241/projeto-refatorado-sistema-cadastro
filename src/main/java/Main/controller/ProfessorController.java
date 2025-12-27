package Main.controller;

import Main.model.Professor;
import Main.model.Usuario.Perfil;
import Main.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {
    
    @Autowired
    private ProfessorService service;
    
    @GetMapping
    public ResponseEntity<List<Professor>> listar(@RequestParam String perfil) {
        try {
            List<Professor> professores = service.listarTodos(Perfil.valueOf(perfil));
            return ResponseEntity.ok(professores);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Professor professor, @RequestParam String perfil) {
        try {
            Professor novoProfessor = (Professor) service.cadastrar(professor, Perfil.valueOf(perfil));
            return ResponseEntity.ok(novoProfessor);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Permissão negada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Professor professor, @RequestParam String perfil) {
        try {
            Professor professorEditado = (Professor) service.editar(id, professor, Perfil.valueOf(perfil));
            return ResponseEntity.ok(professorEditado);
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