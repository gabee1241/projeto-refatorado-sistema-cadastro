package Main.service;

import Main.model.Aluno;
import Main.model.Usuario.Perfil;
import Main.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    
    @Autowired
    private AlunoRepository repository;
    
    @Autowired
    private PermissaoService permissaoService;
    
    public List<Aluno> listarTodos(Perfil perfil) {
        if (!permissaoService.podeConsultar(perfil)) {
            throw new SecurityException("Permissão negada para consultar");
        }
        return repository.findAll();
    }
    
    public Aluno cadastrar(Aluno aluno, Perfil perfil) {
        if (!permissaoService.podeCadastrar(perfil)) {
            throw new SecurityException("Permissão negada para cadastrar");
        }
        if (repository.existsByRg(aluno.getRg())) {
            throw new IllegalArgumentException("RG já cadastrado");
        }
        return repository.save(aluno);
    }
    
    public Aluno editar(Long id, Aluno alunoAtualizado, Perfil perfil) {
        if (!permissaoService.podeEditar(perfil)) {
            throw new SecurityException("Permissão negada para editar");
        }
        Optional<Aluno> alunoExistente = repository.findById(id);
        if (alunoExistente.isEmpty()) {
            throw new IllegalArgumentException("Aluno não encontrado");
        }
        alunoAtualizado.setId(id);
        return repository.save(alunoAtualizado);
    }
    
    public void excluir(Long id, Perfil perfil) {
        if (!permissaoService.podeExcluir(perfil)) {
            throw new SecurityException("Permissão negada para excluir");
        }
        repository.deleteById(id);
    }
}