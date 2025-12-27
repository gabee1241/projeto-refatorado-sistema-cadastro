package Main.service;

import Main.model.Professor;
import Main.model.Usuario.Perfil;
import Main.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository repository;
    
    @Autowired
    private PermissaoService permissaoService;
    
    public List<Professor> listarTodos(Perfil perfil) {
        if (!permissaoService.podeConsultar(perfil)) {
            throw new SecurityException("Permissão negada para consultar");
        }
        return repository.findAll();
    }
    
    public Professor cadastrar(Professor professor, Perfil perfil) {
        if (!permissaoService.podeCadastrar(perfil)) {
            throw new SecurityException("Permissão negada para cadastrar");
        }
        if (repository.existsByRg(professor.getRg())) {
            throw new IllegalArgumentException("RG já cadastrado");
        }
        return repository.save(professor);
    }
    
    public Professor editar(Long id, Professor professorAtualizado, Perfil perfil) {
        if (!permissaoService.podeEditar(perfil)) {
            throw new SecurityException("Permissão negada para editar");
        }
        Optional<Professor> professorExistente = repository.findById(id);
        if (professorExistente.isEmpty()) {
            throw new IllegalArgumentException("Professor não encontrado");
        }
        professorAtualizado.setId(id);
        return repository.save(professorAtualizado);
    }
    
    public void excluir(Long id, Perfil perfil) {
        if (!permissaoService.podeExcluir(perfil)) {
            throw new SecurityException("Permissão negada para excluir");
        }
        repository.deleteById(id);
    }
}